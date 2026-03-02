package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.LoginRequestDto;
import com.ecommerce.NexBuy.dto.request.RegisterRequestDto;
import com.ecommerce.NexBuy.dto.response.AuthResponseDto;
import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Role;
import com.ecommerce.NexBuy.entity.Role.RoleName;
import com.ecommerce.NexBuy.entity.User;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.RoleRepository;
import com.ecommerce.NexBuy.repo.UserRepository;
import com.ecommerce.NexBuy.security.JwtTokenProvider;
import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.security.UserDetailsServiceImpl;
import com.ecommerce.NexBuy.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public AuthResponseDto login(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return AuthResponseDto.builder()
                .token(jwt)
                .type("Bearer")
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public MessageResponseDto register(RegisterRequestDto registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email is already in use: " + registerRequest.getEmail());
        }

        // Create new user
        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setMobileNumber(registerRequest.getMobileNumber());
        user.setEnabled(true);

        // Determine role
        Set<Role> roles = new HashSet<>();
        String requestedRole = registerRequest.getRole();

        if (requestedRole != null && !requestedRole.isBlank()) {
            switch (requestedRole.toUpperCase()) {
                case "ADMIN":
                    Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
                    roles.add(adminRole);
                    break;
                case "SELLER":
                    Role sellerRole = roleRepository.findByName(RoleName.ROLE_SELLER)
                            .orElseThrow(() -> new RuntimeException("Role SELLER not found"));
                    roles.add(sellerRole);
                    break;
                default:
                    Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                            .orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"));
                    roles.add(customerRole);
                    break;
            }
        } else {
            Role customerRole = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Role CUSTOMER not found"));
            roles.add(customerRole);
        }

        user.setRoles(roles);

        // If role is CUSTOMER, also create a linked Customer entity
        if (roles.stream().anyMatch(r -> r.getName() == RoleName.ROLE_CUSTOMER)) {
            Customer customer = new Customer();
            customer.setFirstName(registerRequest.getFirstName());
            customer.setLastName(registerRequest.getLastName());
            customer.setEmail(registerRequest.getEmail());
            customer.setMobileNumber(registerRequest.getMobileNumber());
            customer = customerRepository.save(customer);
            user.setCustomer(customer);
        }

        userRepository.save(user);

        return new MessageResponseDto("User registered successfully!");
    }

    @Override
    public AuthResponseDto refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        String newToken = jwtTokenProvider.generateToken(authentication);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return AuthResponseDto.builder()
                .token(newToken)
                .type("Bearer")
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .roles(roles)
                .build();
    }
}

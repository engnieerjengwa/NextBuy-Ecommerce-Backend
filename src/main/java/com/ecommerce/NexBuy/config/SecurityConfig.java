package com.ecommerce.NexBuy.config;

import com.ecommerce.NexBuy.security.JwtAuthenticationEntryPoint;
import com.ecommerce.NexBuy.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${allowed.origins}")
    private String[] allowedOrigins;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                // Auth endpoints - public
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/api/auth/google").permitAll()
                .requestMatchers("/api/auth/refresh-token").permitAll()
                // Admin endpoints - require ADMIN role
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/auth/register-privileged").hasRole("ADMIN")
                // Order endpoints - require authentication
                .requestMatchers("/api/orders/**").authenticated()
                .requestMatchers("/api/checkout/purchase").authenticated()
                .requestMatchers("/api/checkout/payment-intent").authenticated()
                .requestMatchers("/api/auth/me").authenticated()
                // Wishlist & Addresses - require authentication
                .requestMatchers("/api/wishlist/**").authenticated()
                .requestMatchers("/api/addresses/**").authenticated()
                // Reviews - auth required for write operations, public for reads
                .requestMatchers(HttpMethod.POST, "/api/products/*/reviews").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/reviews/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/reviews/**").authenticated()
                .requestMatchers("/api/reviews/my").authenticated()
                // Product Q&A - auth required for write operations
                .requestMatchers(HttpMethod.POST, "/api/products/*/questions").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/questions/*/answers").authenticated()
                // Coupons - validate is public, apply requires auth
                .requestMatchers(HttpMethod.POST, "/api/coupons/validate").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/coupons/apply").authenticated()
                // Deals - public
                .requestMatchers(HttpMethod.GET, "/api/deals/**").permitAll()
                // Invoice - auth required
                .requestMatchers("/api/orders/*/invoice").authenticated()
                // Recently Viewed - auth required
                .requestMatchers("/api/recently-viewed/**").authenticated()
                // Newsletter - public
                .requestMatchers("/api/newsletter/**").permitAll()
                // Gift Cards - check is public, purchase/redeem/my-cards require auth
                .requestMatchers(HttpMethod.GET, "/api/gift-cards/check/**").permitAll()
                .requestMatchers("/api/gift-cards/**").authenticated()
                // Wallet - auth required
                .requestMatchers("/api/wallet/**").authenticated()
                // Loyalty - auth required
                .requestMatchers("/api/loyalty/**").authenticated()
                // Referrals - auth required
                .requestMatchers("/api/referrals/**").authenticated()
                // PreOrders - list/status is public, place requires auth
                .requestMatchers(HttpMethod.GET, "/api/preorders/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/preorders/**").authenticated()
                // Frequently Bought Together - public
                .requestMatchers(HttpMethod.GET, "/api/products/*/frequently-bought-together").permitAll()
                // Review seller response - admin only (handled via @PreAuthorize)
                .requestMatchers(HttpMethod.POST, "/api/reviews/*/respond").hasRole("ADMIN")
                // Public endpoints
                .requestMatchers(HttpMethod.GET, "/api/products/*/reviews/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/reviews/*/helpful").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/products/*/questions").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/answers/*/helpful").permitAll()
                .requestMatchers("/api/products/*/notify-restock").permitAll()
                .requestMatchers("/api/checkout/guest").permitAll()
                .requestMatchers("/api/checkout/validate-stock").permitAll()
                .requestMatchers("/api/products/search/**").permitAll()
                .requestMatchers("/api/products/autocomplete/**").permitAll()
                .requestMatchers("/api/products/brands/**").permitAll()
                .requestMatchers("/api/products/featured/**").permitAll()
                .requestMatchers("/api/products/new-arrivals/**").permitAll()
                .requestMatchers("/api/products/*/images").permitAll()
                .requestMatchers("/api/products/*/variants").permitAll()
                .anyRequest().permitAll());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(allowedOrigins));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

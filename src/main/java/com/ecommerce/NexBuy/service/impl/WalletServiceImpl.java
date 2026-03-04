package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.response.WalletResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.CustomerWallet;
import com.ecommerce.NexBuy.entity.WalletTransaction;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.CustomerWalletRepository;
import com.ecommerce.NexBuy.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WalletServiceImpl implements WalletService {

    private static final Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    private final CustomerWalletRepository customerWalletRepository;
    private final CustomerRepository customerRepository;

    public WalletServiceImpl(CustomerWalletRepository customerWalletRepository,
                             CustomerRepository customerRepository) {
        this.customerWalletRepository = customerWalletRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponseDto getWallet(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        CustomerWallet wallet = customerWalletRepository.findByCustomerId(customer.getId())
                .orElse(null);

        if (wallet == null) {
            // Return empty wallet DTO
            WalletResponseDto dto = new WalletResponseDto();
            dto.setBalance(BigDecimal.ZERO);
            dto.setCurrency("USD");
            dto.setLastUpdated(null);
            dto.setRecentTransactions(List.of());
            return dto;
        }

        return mapToResponseDto(wallet);
    }

    @Override
    @Transactional
    public WalletResponseDto getOrCreateWallet(String customerEmail) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        CustomerWallet wallet = customerWalletRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    CustomerWallet newWallet = new CustomerWallet();
                    newWallet.setCustomer(customer);
                    newWallet.setBalance(BigDecimal.ZERO);
                    newWallet.setCurrency("USD");
                    return customerWalletRepository.save(newWallet);
                });

        return mapToResponseDto(wallet);
    }

    private WalletResponseDto mapToResponseDto(CustomerWallet wallet) {
        WalletResponseDto dto = new WalletResponseDto();
        dto.setBalance(wallet.getBalance());
        dto.setCurrency(wallet.getCurrency());
        dto.setLastUpdated(wallet.getLastUpdated());

        List<WalletResponseDto.WalletTransactionDto> transactions = wallet.getTransactions().stream()
                .sorted((a, b) -> b.getDateCreated().compareTo(a.getDateCreated()))
                .limit(20)
                .map(this::mapTransactionToDto)
                .collect(Collectors.toList());

        dto.setRecentTransactions(transactions);
        return dto;
    }

    private WalletResponseDto.WalletTransactionDto mapTransactionToDto(WalletTransaction tx) {
        WalletResponseDto.WalletTransactionDto dto = new WalletResponseDto.WalletTransactionDto();
        dto.setId(tx.getId());
        dto.setAmount(tx.getAmount());
        dto.setType(tx.getType().name());
        dto.setSource(tx.getSource().name());
        dto.setReferenceId(tx.getReferenceId());
        dto.setDescription(tx.getDescription());
        dto.setDateCreated(tx.getDateCreated());
        return dto;
    }
}

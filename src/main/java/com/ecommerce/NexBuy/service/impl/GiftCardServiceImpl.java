package com.ecommerce.NexBuy.service.impl;

import com.ecommerce.NexBuy.dto.request.GiftCardPurchaseRequestDto;
import com.ecommerce.NexBuy.dto.request.GiftCardRedeemRequestDto;
import com.ecommerce.NexBuy.dto.response.GiftCardResponseDto;
import com.ecommerce.NexBuy.dto.response.MessageResponseDto;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.CustomerWallet;
import com.ecommerce.NexBuy.entity.GiftCard;
import com.ecommerce.NexBuy.entity.WalletTransaction;
import com.ecommerce.NexBuy.repo.CustomerRepository;
import com.ecommerce.NexBuy.repo.CustomerWalletRepository;
import com.ecommerce.NexBuy.repo.GiftCardRepository;
import com.ecommerce.NexBuy.service.GiftCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GiftCardServiceImpl implements GiftCardService {

    private static final Logger logger = LoggerFactory.getLogger(GiftCardServiceImpl.class);

    private final GiftCardRepository giftCardRepository;
    private final CustomerRepository customerRepository;
    private final CustomerWalletRepository customerWalletRepository;

    public GiftCardServiceImpl(GiftCardRepository giftCardRepository,
                               CustomerRepository customerRepository,
                               CustomerWalletRepository customerWalletRepository) {
        this.giftCardRepository = giftCardRepository;
        this.customerRepository = customerRepository;
        this.customerWalletRepository = customerWalletRepository;
    }

    @Override
    @Transactional
    public GiftCardResponseDto purchaseGiftCard(String purchaserEmail, GiftCardPurchaseRequestDto request) {
        Customer purchaser = customerRepository.findByEmail(purchaserEmail);
        if (purchaser == null) {
            throw new IllegalArgumentException("Customer not found with email: " + purchaserEmail);
        }

        if (request.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Gift card amount must be at least $1.00");
        }

        if (request.getAmount().compareTo(new BigDecimal("500")) > 0) {
            throw new IllegalArgumentException("Gift card amount cannot exceed $500.00");
        }

        GiftCard giftCard = new GiftCard();
        giftCard.setCode(generateGiftCardCode());
        giftCard.setInitialAmount(request.getAmount());
        giftCard.setRemainingAmount(request.getAmount());
        giftCard.setCurrency("USD");
        giftCard.setPurchaser(purchaser);
        giftCard.setRecipientEmail(request.getRecipientEmail());
        giftCard.setPersonalMessage(request.getPersonalMessage());
        giftCard.setStatus(GiftCard.GiftCardStatus.ACTIVE);
        giftCard.setExpiryDate(LocalDate.now().plusYears(1));

        GiftCard saved = giftCardRepository.save(giftCard);

        logger.info("Gift card purchased: code={}, amount={}, purchaser={}", saved.getCode(), saved.getInitialAmount(), purchaserEmail);

        return mapToResponseDto(saved);
    }

    @Override
    @Transactional
    public MessageResponseDto redeemGiftCard(String customerEmail, GiftCardRedeemRequestDto request) {
        Customer customer = customerRepository.findByEmail(customerEmail);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + customerEmail);
        }

        GiftCard giftCard = giftCardRepository.findByCodeIgnoreCase(request.getCode())
                .orElseThrow(() -> new IllegalArgumentException("Gift card not found with code: " + request.getCode()));

        if (giftCard.getStatus() != GiftCard.GiftCardStatus.ACTIVE) {
            throw new IllegalArgumentException("Gift card is not active. Status: " + giftCard.getStatus());
        }

        if (giftCard.getExpiryDate() != null && giftCard.getExpiryDate().isBefore(LocalDate.now())) {
            giftCard.setStatus(GiftCard.GiftCardStatus.EXPIRED);
            giftCardRepository.save(giftCard);
            throw new IllegalArgumentException("Gift card has expired");
        }

        if (giftCard.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Gift card has no remaining balance");
        }

        BigDecimal redeemAmount = giftCard.getRemainingAmount();

        // Credit to customer wallet
        CustomerWallet wallet = customerWalletRepository.findByCustomerId(customer.getId())
                .orElseGet(() -> {
                    CustomerWallet newWallet = new CustomerWallet();
                    newWallet.setCustomer(customer);
                    newWallet.setBalance(BigDecimal.ZERO);
                    newWallet.setCurrency("USD");
                    return customerWalletRepository.save(newWallet);
                });

        wallet.setBalance(wallet.getBalance().add(redeemAmount));
        wallet.setLastUpdated(LocalDateTime.now());

        WalletTransaction transaction = new WalletTransaction();
        transaction.setWallet(wallet);
        transaction.setAmount(redeemAmount);
        transaction.setType(WalletTransaction.TransactionType.CREDIT);
        transaction.setSource(WalletTransaction.TransactionSource.GIFT_CARD);
        transaction.setReferenceId(giftCard.getCode());
        transaction.setDescription("Gift card redeemed: " + giftCard.getCode());
        wallet.getTransactions().add(transaction);

        customerWalletRepository.save(wallet);

        // Update gift card
        giftCard.setRemainingAmount(BigDecimal.ZERO);
        giftCard.setStatus(GiftCard.GiftCardStatus.REDEEMED);
        giftCardRepository.save(giftCard);

        logger.info("Gift card redeemed: code={}, amount={}, customer={}", giftCard.getCode(), redeemAmount, customerEmail);

        return new MessageResponseDto(String.format("Gift card redeemed! $%.2f has been added to your wallet.", redeemAmount));
    }

    @Override
    @Transactional(readOnly = true)
    public GiftCardResponseDto getGiftCardByCode(String code) {
        GiftCard giftCard = giftCardRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> new IllegalArgumentException("Gift card not found with code: " + code));
        return mapToResponseDto(giftCard);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GiftCardResponseDto> getMyGiftCards(String email) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found with email: " + email);
        }

        return giftCardRepository.findByPurchaserId(customer.getId()).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private GiftCardResponseDto mapToResponseDto(GiftCard giftCard) {
        GiftCardResponseDto dto = new GiftCardResponseDto();
        dto.setId(giftCard.getId());
        dto.setCode(giftCard.getCode());
        dto.setInitialAmount(giftCard.getInitialAmount());
        dto.setRemainingAmount(giftCard.getRemainingAmount());
        dto.setCurrency(giftCard.getCurrency());
        dto.setRecipientEmail(giftCard.getRecipientEmail());
        dto.setPersonalMessage(giftCard.getPersonalMessage());
        dto.setStatus(giftCard.getStatus().name());
        dto.setExpiryDate(giftCard.getExpiryDate());
        return dto;
    }

    private String generateGiftCardCode() {
        String code = "GC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        while (giftCardRepository.findByCodeIgnoreCase(code).isPresent()) {
            code = "GC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        return code;
    }
}

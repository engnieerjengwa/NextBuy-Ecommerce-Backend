package com.ecommerce.NexBuy.service;

/**
 * Service interface for sending emails
 */
public interface EmailService {
    
    /**
     * Send a receipt email to a customer after successful payment
     * 
     * @param to The recipient's email address
     * @param subject The email subject
     * @param customerName The customer's name
     * @param orderTrackingNumber The order tracking number
     * @param totalPrice The total price of the order
     * @param totalQuantity The total quantity of items in the order
     */
    void sendReceiptEmail(String to, String subject, String customerName, 
                         String orderTrackingNumber, double totalPrice, int totalQuantity);

    /**
     * Send a back-in-stock notification email
     *
     * @param to The recipient's email address
     * @param productName The product name that is back in stock
     * @param productId The product ID
     */
    void sendStockNotificationEmail(String to, String productName, Long productId);
}
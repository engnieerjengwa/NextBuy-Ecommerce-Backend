package com.ecommerce.NexBuy.service;

public interface InvoiceService {

    byte[] generateInvoicePdf(Long orderId, String customerEmail);
}

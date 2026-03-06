package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.security.UserDetailsImpl;
import com.ecommerce.NexBuy.service.InvoiceService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Invoices", description = "Order invoice PDF generation and download")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @Operation(summary = "Download invoice", description = "Generate and download a PDF invoice for an order")
    @GetMapping("/orders/{orderId}/invoice")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> downloadInvoice(
            @PathVariable Long orderId,
            Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        byte[] pdfBytes = invoiceService.generateInvoicePdf(orderId, userDetails.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "invoice-" + orderId + ".pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}

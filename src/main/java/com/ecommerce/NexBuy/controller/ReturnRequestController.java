package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.ReturnRequestRequestDto;
import com.ecommerce.NexBuy.dto.response.ReturnRequestResponseDto;
import com.ecommerce.NexBuy.service.ReturnRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/returns")
public class ReturnRequestController {

    private final ReturnRequestService returnRequestService;

    @Autowired
    public ReturnRequestController(ReturnRequestService returnRequestService) {
        this.returnRequestService = returnRequestService;
    }

    /**
     * Create a new return request
     * @param returnRequestRequestDto the return request data
     * @return the created return request
     */
    @PostMapping
    public ResponseEntity<ReturnRequestResponseDto> createReturnRequest(
            @RequestBody ReturnRequestRequestDto returnRequestRequestDto) {
        ReturnRequestResponseDto responseDto = returnRequestService.createReturnRequest(returnRequestRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * Get a return request by ID
     * @param id the return request ID
     * @return the return request
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReturnRequestResponseDto> getReturnRequestById(@PathVariable Long id) {
        ReturnRequestResponseDto responseDto = returnRequestService.getReturnRequestById(id);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * Get all return requests for an order
     * @param orderId the order ID
     * @param pageable pagination information
     * @return a page of return requests
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<ReturnRequestResponseDto>> getReturnRequestsByOrderId(
            @PathVariable Long orderId, Pageable pageable) {
        Page<ReturnRequestResponseDto> responseDtos = returnRequestService.getReturnRequestsByOrderId(orderId, pageable);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Get all return requests for a customer
     * @param email the customer's email
     * @param pageable pagination information
     * @return a page of return requests
     */
    @GetMapping("/customer")
    public ResponseEntity<Page<ReturnRequestResponseDto>> getReturnRequestsByCustomerEmail(
            @RequestParam String email, Pageable pageable) {
        Page<ReturnRequestResponseDto> responseDtos = returnRequestService.getReturnRequestsByCustomerEmail(email, pageable);
        return ResponseEntity.ok(responseDtos);
    }

    /**
     * Update the status of a return request
     * @param id the return request ID
     * @param status the new status
     * @return the updated return request
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReturnRequestResponseDto> updateReturnRequestStatus(
            @PathVariable Long id, @RequestParam String status) {
        ReturnRequestResponseDto responseDto = returnRequestService.updateReturnRequestStatus(id, status);
        return ResponseEntity.ok(responseDto);
    }
}
package com.ecommerce.NexBuy.service;

import com.ecommerce.NexBuy.dto.request.ReturnRequestRequestDto;
import com.ecommerce.NexBuy.dto.response.ReturnRequestResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReturnRequestService {
    
    /**
     * Create a new return request
     * @param returnRequestRequestDto the return request data
     * @return the created return request
     */
    ReturnRequestResponseDto createReturnRequest(ReturnRequestRequestDto returnRequestRequestDto);
    
    /**
     * Get a return request by ID
     * @param id the return request ID
     * @return the return request
     */
    ReturnRequestResponseDto getReturnRequestById(Long id);
    
    /**
     * Get all return requests for an order
     * @param orderId the order ID
     * @param pageable pagination information
     * @return a page of return requests
     */
    Page<ReturnRequestResponseDto> getReturnRequestsByOrderId(Long orderId, Pageable pageable);
    
    /**
     * Get all return requests for a customer
     * @param email the customer's email
     * @param pageable pagination information
     * @return a page of return requests
     */
    Page<ReturnRequestResponseDto> getReturnRequestsByCustomerEmail(String email, Pageable pageable);
    
    /**
     * Update the status of a return request
     * @param id the return request ID
     * @param status the new status
     * @return the updated return request
     */
    ReturnRequestResponseDto updateReturnRequestStatus(Long id, String status);
}
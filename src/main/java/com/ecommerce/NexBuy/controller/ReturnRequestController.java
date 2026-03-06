package com.ecommerce.NexBuy.controller;

import com.ecommerce.NexBuy.dto.request.ReturnRequestRequestDto;
import com.ecommerce.NexBuy.dto.response.ReturnRequestResponseDto;
import com.ecommerce.NexBuy.service.ReturnRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Returns", description = "Return request submission and status tracking")
@RestController
@RequestMapping("/api/returns")
public class ReturnRequestController {

    private final ReturnRequestService returnRequestService;

    public ReturnRequestController(ReturnRequestService returnRequestService) {
        this.returnRequestService = returnRequestService;
    }

    @Operation(summary = "Create return request", description = "Submit a new return request for an order")
    @PostMapping
    public ResponseEntity<ReturnRequestResponseDto> createReturnRequest(
            @RequestBody ReturnRequestRequestDto returnRequestRequestDto) {
        ReturnRequestResponseDto responseDto = returnRequestService.createReturnRequest(returnRequestRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @Operation(summary = "Get return request", description = "Retrieve a return request by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReturnRequestResponseDto> getReturnRequestById(@PathVariable Long id) {
        ReturnRequestResponseDto responseDto = returnRequestService.getReturnRequestById(id);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "Get returns by order", description = "Retrieve all return requests for an order")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Page<ReturnRequestResponseDto>> getReturnRequestsByOrderId(
            @PathVariable Long orderId, Pageable pageable) {
        Page<ReturnRequestResponseDto> responseDtos = returnRequestService.getReturnRequestsByOrderId(orderId, pageable);
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(summary = "Get returns by customer", description = "Retrieve all return requests for a customer by email")
    @GetMapping("/customer")
    public ResponseEntity<Page<ReturnRequestResponseDto>> getReturnRequestsByCustomerEmail(
            @RequestParam String email, Pageable pageable) {
        Page<ReturnRequestResponseDto> responseDtos = returnRequestService.getReturnRequestsByCustomerEmail(email, pageable);
        return ResponseEntity.ok(responseDtos);
    }

    @Operation(summary = "Update return status", description = "Update the status of a return request")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ReturnRequestResponseDto> updateReturnRequestStatus(
            @PathVariable Long id, @RequestParam String status) {
        ReturnRequestResponseDto responseDto = returnRequestService.updateReturnRequestStatus(id, status);
        return ResponseEntity.ok(responseDto);
    }
}
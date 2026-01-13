package com.ecommerce.NexBuy.dto.request;

import com.ecommerce.NexBuy.entity.Address;
import com.ecommerce.NexBuy.entity.Customer;
import com.ecommerce.NexBuy.entity.Order;
import com.ecommerce.NexBuy.entity.OrderItem;
import lombok.Data;

import java.util.Set;

@Data
public class PurchaseRequestDto {

    private Customer customer;
    private Address billingAddress;
    private Address shippingAddress;
    private Order order;
    private Set<OrderItem> orderItems;
}

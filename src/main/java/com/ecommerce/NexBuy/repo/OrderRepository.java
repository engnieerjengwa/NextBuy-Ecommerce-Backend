package com.ecommerce.NexBuy.repo;

import com.ecommerce.NexBuy.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("http://localhost:4200")
@RepositoryRestResource(path = "orders")
public interface OrderRepository extends JpaRepository<Order, Long> {
}
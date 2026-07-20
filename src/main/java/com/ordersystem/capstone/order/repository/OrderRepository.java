package com.ordersystem.capstone.order.repository;

import com.ordersystem.capstone.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository
        extends JpaRepository<Order, UUID> {
}
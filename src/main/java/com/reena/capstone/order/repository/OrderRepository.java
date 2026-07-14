package com.reena.capstone.order.repository;

import com.reena.capstone.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository
        extends JpaRepository<Order, UUID> {
}
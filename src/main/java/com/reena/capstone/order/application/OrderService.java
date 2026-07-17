package com.reena.capstone.order.application;

import com.reena.capstone.order.domain.Order;

import java.util.List;
import java.util.UUID;


public interface OrderService {

    public Order createOrder(Order order) ;

    public Order getOrder(UUID orderId);

    public List<Order> getAllOrders();

    public Order updateOrder(Order order);
}
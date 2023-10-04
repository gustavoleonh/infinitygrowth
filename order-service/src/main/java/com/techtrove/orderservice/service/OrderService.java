package com.techtrove.orderservice.service;

import com.techtrove.orderservice.dto.OrderRequest;
import com.techtrove.orderservice.dto.OrderResponse;
import com.techtrove.orderservice.model.Order;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(String id);

    String placeOrder(OrderRequest orderRequest);
}

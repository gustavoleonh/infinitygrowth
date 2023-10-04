package com.techtrove.orderservice.service.impl;

import com.techtrove.orderservice.dto.InventoryRequest;
import com.techtrove.orderservice.dto.InventoryResponse;
import com.techtrove.orderservice.dto.InventoryValidationRequest;
import com.techtrove.orderservice.dto.InventoryValidationResponse;
import com.techtrove.orderservice.dto.OrderItemsDTO;
import com.techtrove.orderservice.dto.OrderRequest;
import com.techtrove.orderservice.dto.OrderResponse;
import com.techtrove.orderservice.exception.OrderNotFoundException;
import com.techtrove.orderservice.model.Order;
import com.techtrove.orderservice.model.OrderItem;
import com.techtrove.orderservice.repository.OrderRepository;
import com.techtrove.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final String INVENTORY_ENDPOINT = "api/inventory";

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    private final String ORDER_PLACED_MESSAGE = "Order placed";

    private final String OUT_OF_STOCK_MESSAGE = "Some products are not available, please try again later";

    @Value("${api.support-services.product-service.url}")
    private String productServiceUrl;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        Order order = mapToOrder(orderRequest);
        return mapToOrderResponse(orderRepository.save(order));
    }

    public List<OrderResponse> getAllOrders() {

        List<Order> orderList = orderRepository.findAll();

        return orderList.stream().map(this::mapToOrderResponse).collect(Collectors.toList());

    }

    public OrderResponse getOrderById(String id) {

        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            return mapToOrderResponse(order.get());
        } else {
            throw new OrderNotFoundException("Order not found.");
        }
    }

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItem> orderItems = orderRequest.getOrderItemsDTOList()
                .stream()
                .map(this::mapToOrderItem)
                .collect(Collectors.toList());

        order.setOrderItemsList(orderItems);

        if (productsInStock(orderRequest)) {
            orderRepository.save(order);
            return ORDER_PLACED_MESSAGE;
        } else {
            // throw new IllegalArgumentException("Some products are not available, please try again later");
            return OUT_OF_STOCK_MESSAGE;
        }
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder().build();
    }

    private Order mapToOrder(OrderRequest orderRequest) {
        return Order.builder().build();
    }

    private OrderItem mapToOrderItem(OrderItemsDTO orderItemsDTO) {
        return OrderItem.builder()
                .skuCode(orderItemsDTO.getSku())
                .quantity(orderItemsDTO.getQty())
                .price(orderItemsDTO.getPrice())
                .build();
    }

    private boolean productsInStock(OrderRequest orderRequest) {
        final InventoryValidationResponse inventoryValidationResponse = webClientBuilder.build()
                .post()
                .uri(productServiceUrl + INVENTORY_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(mapToInventoryValidationRequest(orderRequest)))
                .retrieve().bodyToMono(InventoryValidationResponse.class)
                .block();

        return allProductsInStock(inventoryValidationResponse);

    }

    private InventoryValidationRequest mapToInventoryValidationRequest(OrderRequest orderRequest) {
        final List<InventoryRequest> inventoryRequestList = orderRequest.getOrderItemsDTOList()
                .stream()
                .map(orderItemsDTO -> InventoryRequest.builder().sku(orderItemsDTO.getSku()).qty(orderItemsDTO.getQty()).build())
                .collect(Collectors.toList());

        return InventoryValidationRequest.builder().inventoryRequestList(inventoryRequestList).build();
    }

    private boolean allProductsInStock(InventoryValidationResponse inventoryValidationResponse) {
        boolean allProductsInStock = true;

        for (InventoryResponse inventoryResponse : inventoryValidationResponse.getInventoryResponseList()) {
            if (!inventoryResponse.isInStock()) {
                allProductsInStock = false;
                break;
            }
        }

        return allProductsInStock;
    }

}
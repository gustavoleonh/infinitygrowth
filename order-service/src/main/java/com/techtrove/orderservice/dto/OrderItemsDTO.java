package com.techtrove.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemsDTO {
    private Long id;
    private String sku;
    private BigDecimal price;
    private Integer qty;
}

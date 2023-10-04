package com.techtrove.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Inventory {
    private String sku;
    private Integer qty;
}

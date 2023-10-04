package com.techtrove.productservice.repository;

import com.techtrove.productservice.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findBySkuIn(List<String> sku);
}

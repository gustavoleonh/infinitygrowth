package com.techtrove.productservice.service;

import com.techtrove.productservice.dto.InventoryRequest;
import com.techtrove.productservice.dto.InventoryResponse;
import com.techtrove.productservice.dto.InventoryValidationResponse;

import java.util.List;

public interface InventoryService {
    InventoryValidationResponse isProductInStock(List<InventoryRequest> skuList);

    void createInventoryRecord(InventoryRequest inventoryRequest);

}

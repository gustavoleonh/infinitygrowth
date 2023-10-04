package com.techtrove.productservice.service.impl;

import com.techtrove.productservice.dto.InventoryRequest;
import com.techtrove.productservice.dto.InventoryResponse;
import com.techtrove.productservice.dto.InventoryValidationResponse;
import com.techtrove.productservice.model.Inventory;
import com.techtrove.productservice.repository.InventoryRepository;
import com.techtrove.productservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryValidationResponse isProductInStock(List<InventoryRequest> skuList) {
        log.info("Validating product stock");

        Map<String, InventoryRequest> inventoryRequestMap = skuList.stream().collect(Collectors.toMap(InventoryRequest::getSku, inventoryRequest -> inventoryRequest));
        List<Inventory> inventoryList = inventoryRepository.findBySkuIn(skuList.stream().map(InventoryRequest::getSku).collect(Collectors.toList()));
        Map<String, Inventory> mapInventories = inventoryList.stream().collect(Collectors.toMap(Inventory::getSku, inventory -> inventory));

        List<InventoryResponse> inventoryResponseList = new ArrayList<>();

        inventoryRequestMap.values().stream().forEach(
                inventoryRequest -> inventoryResponseList.add(
                        InventoryResponse.builder()
                                .sku(inventoryRequest.getSku())
                                .isInStock(mapInventories.get(inventoryRequest.getSku()).getQty() >= inventoryRequest.getQty()).build())
        );

        return InventoryValidationResponse.builder().inventoryResponseList(inventoryResponseList).build();

    }

    @Override
    public void createInventoryRecord(InventoryRequest inventoryRequest) {
        inventoryRepository.save(Inventory.builder().sku(inventoryRequest.getSku()).qty(inventoryRequest.getQty()).build());
    }

}

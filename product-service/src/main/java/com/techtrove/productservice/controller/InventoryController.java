package com.techtrove.productservice.controller;

import com.techtrove.productservice.dto.InventoryRequest;
import com.techtrove.productservice.dto.InventoryValidationRequest;
import com.techtrove.productservice.dto.InventoryValidationResponse;
import com.techtrove.productservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api/inventory")
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping//Using post in purpose
    @ResponseStatus(HttpStatus.OK)
    public InventoryValidationResponse isInStock(@RequestBody InventoryValidationRequest inventoryRequestList) {
        log.info("Received inventory check request for skuCode: {}", inventoryRequestList);
        return inventoryService.isProductInStock(inventoryRequestList.getInventoryRequestList());
    }

    @PostMapping(value = "/record")
    @ResponseStatus(HttpStatus.CREATED)
    public void createInventoryRecord(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.createInventoryRecord(inventoryRequest);
    }
}

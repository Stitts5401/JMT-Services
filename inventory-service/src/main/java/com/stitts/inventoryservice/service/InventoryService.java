package com.stitts.inventoryservice.service;

import com.stitts.inventoryservice.model.Inventory;
import com.stitts.inventoryservice.repository.InventoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    public boolean isInStock(String skuCode){
       return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }
}

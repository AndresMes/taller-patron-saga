package edu.unimagdalena.inventoryservice.service;

import edu.unimagdalena.inventoryservice.DTO.ReserveInventoryCommand;
import edu.unimagdalena.inventoryservice.model.InventoryItem;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryItemService {
    List<InventoryItem> get_all();
    InventoryItem getById(Long id);
    InventoryItem create(InventoryItem item);
    InventoryItem update(InventoryItem item, Long id);
    void delete(Long id);

    boolean reserveInventory(ReserveInventoryCommand command);
    Long getAvailableQuantity(String productId);  // ⚠️ CAMBIO: era Long, ahora String
    BigDecimal getProductPrice(String productId);  // ⚠️ NUEVO MÉTODO NECESARIO
}
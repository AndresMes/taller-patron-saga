package edu.unimagdalena.inventoryservice.service;

import edu.unimagdalena.inventoryservice.DTO.ReserveInventoryCommand;
import edu.unimagdalena.inventoryservice.model.InventoryItem;

import java.util.List;

public interface InventoryItemService {

    List<InventoryItem> get_all();
    InventoryItem getById(Long id);
    InventoryItem create(InventoryItem item);
    InventoryItem update(InventoryItem item, Long id);
    void delete(Long id);


    public boolean reserveInventory(ReserveInventoryCommand command);
    public Long getAvailableQuantity(Long productId);
}

package edu.unimagdalena.inventoryservice.repository;

import edu.unimagdalena.inventoryservice.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {


}

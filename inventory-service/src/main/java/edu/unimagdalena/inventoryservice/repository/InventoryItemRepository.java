package edu.unimagdalena.inventoryservice.repository;

import edu.unimagdalena.inventoryservice.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByIdProduct(String idProduct);  // ⚠️ NUEVO MÉTODO
}
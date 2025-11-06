package edu.unimagdalena.inventoryservice.service.impl;

import edu.unimagdalena.inventoryservice.DTO.ReserveInventoryCommand;
import edu.unimagdalena.inventoryservice.exception.ItemNotFoundException;
import edu.unimagdalena.inventoryservice.model.InventoryItem;
import edu.unimagdalena.inventoryservice.repository.InventoryItemRepository;
import edu.unimagdalena.inventoryservice.service.InventoryItemService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryItemServiceImpl implements InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public List<InventoryItem> get_all() {
        return inventoryItemRepository.findAll();
    }

    @Override
    public InventoryItem getById(Long id) {
        return inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item con id: " + id + " no encontrado"));
    }

    @Override
    public InventoryItem create(InventoryItem item) {
        return inventoryItemRepository.save(item);
    }

    @Override
    public InventoryItem update(InventoryItem item, Long id) {
        InventoryItem baseItem = inventoryItemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item con id: " + id + " no encontrado"));

        if (item.getPrice() != null) baseItem.setPrice(item.getPrice());
        if (item.getIdProduct() != null) baseItem.setIdProduct(item.getIdProduct());
        if (item.getAvailableQuantity() != null) baseItem.setAvailableQuantity(item.getAvailableQuantity());

        return inventoryItemRepository.save(baseItem);
    }

    @Override
    public void delete(Long id) {
        if (!inventoryItemRepository.existsById(id)) {
            throw new ItemNotFoundException("Item con id: " + id + " no encontrado");
        }
        inventoryItemRepository.deleteById(id);
    }

    @Override
    @Transactional
    public boolean reserveInventory(ReserveInventoryCommand command) {
        // ⚠️ CAMBIO: Buscar por idProduct (String) en lugar de id (Long)
        InventoryItem item = inventoryItemRepository.findByIdProduct(command.getProductId())
                .orElseThrow(() -> new ItemNotFoundException(
                        "Producto con id: " + command.getProductId() + " no encontrado"));

        if (item.getAvailableQuantity() >= command.getQuantity()) {
            item.setAvailableQuantity(item.getAvailableQuantity() - command.getQuantity());
            inventoryItemRepository.save(item);
            return true;
        }

        return false;
    }

    @Override
    public Long getAvailableQuantity(String productId) {
        InventoryItem item = inventoryItemRepository.findByIdProduct(productId)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Producto con id: " + productId + " no encontrado"));
        return item.getAvailableQuantity();
    }

    @Override
    public BigDecimal getProductPrice(String productId) {
        InventoryItem item = inventoryItemRepository.findByIdProduct(productId)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Producto con id: " + productId + " no encontrado"));
        return item.getPrice();
    }
}
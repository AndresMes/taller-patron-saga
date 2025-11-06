package edu.unimagdalena.inventoryservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRejectedEvent {
    private String orderId;
    private String productId;
    private Integer requestedQuantity;
    private Long availableQuantity;
    private String reason;
}
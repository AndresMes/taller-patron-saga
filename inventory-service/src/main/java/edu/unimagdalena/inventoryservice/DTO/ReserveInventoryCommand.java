package edu.unimagdalena.inventoryservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveInventoryCommand {
    private String orderId;
    private String productId;  // ⚠️ CAMBIO: era Long, ahora String
    private Integer quantity;
}
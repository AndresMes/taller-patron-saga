package edu.unimagdalena.inventoryservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Comando recibido
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReserveInventoryCommand {
    private String orderId;
    private Long productId;
    private Integer quantity;
}
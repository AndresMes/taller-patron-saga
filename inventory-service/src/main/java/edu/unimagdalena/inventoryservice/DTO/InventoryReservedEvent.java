package edu.unimagdalena.inventoryservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryReservedEvent {
    private String orderId;
    private String productId;  // ⚠️ CAMBIO: era Long, ahora String
    private Integer quantity;
    private BigDecimal totalAmount;  // ⚠️ NUEVO CAMPO REQUERIDO
}
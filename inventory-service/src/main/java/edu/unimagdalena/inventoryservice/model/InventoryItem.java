package edu.unimagdalena.inventoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInventoryItem;

    @Column
    private String idProduct;

    @Column
    private Long availableQuantity;

    @Column
    private Double price;

}

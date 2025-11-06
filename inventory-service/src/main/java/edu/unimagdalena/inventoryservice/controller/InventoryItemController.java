package edu.unimagdalena.inventoryservice.controller;

import edu.unimagdalena.inventoryservice.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventorys")
@RequiredArgsConstructor
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;
}

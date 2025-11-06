package edu.unimagdalena.orderservice.controller;

import edu.unimagdalena.orderservice.dto.CreateOrderRequest;
import edu.unimagdalena.orderservice.dto.CreateOrderResponse;
import edu.unimagdalena.orderservice.entity.Order;
import edu.unimagdalena.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("Received POST /orders request: {}", request);
        CreateOrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable UUID orderId) {
        log.info("Received GET /orders/{} request", orderId);
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }
}
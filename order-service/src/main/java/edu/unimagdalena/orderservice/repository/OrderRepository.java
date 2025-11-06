package edu.unimagdalena.orderservice.repository;

import edu.unimagdalena.orderservice.entity.Order;
import edu.unimagdalena.orderservice.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {


    List<Order> findByStatus(OrderStatus status);


    List<Order> findByProductId(String productId);


    List<Order> findByProductIdAndStatus(String productId, OrderStatus status);
}
package com.example.ordersservice.datalayer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    // Finds an order by its ID. The return type is Optional<Order> to handle the case where an order is not found.
    Optional<Order> findOrderByOrderIdentifier_OrderId(String orderId);

    // Saves an order and returns the saved order.
    // This method is already provided by JpaRepository but is included here for clarity.
    <S extends Order> S save(S order);

    void delete(Order order);

    // Finds orders by customer ID.
    List<Order> findByCustomerId_CustomerId(String customerId);

    // Finds orders by product ID.
    List<Order> findByItems_ProductId_ProductId(String productId);

}

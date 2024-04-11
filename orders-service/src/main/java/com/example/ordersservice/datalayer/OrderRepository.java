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

    // Deletes a given order. This method is also provided by JpaRepository.
    void delete(Order order);

    // Finds orders by customer ID. This is a custom method that you need to add to match your OrderService implementation.
    List<Order> findByCustomerId(String customerId);

    // You can add more custom methods here if needed, such as methods to find orders by date range, status, etc.
}

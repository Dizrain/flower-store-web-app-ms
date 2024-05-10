package com.example.ordersservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();

        // Create a new Order object
        Order order = new Order();
        order.setOrderIdentifier(new OrderIdentifier());
        order.setStatus(OrderStatus.PLACED);
        order.setCustomerDetails(new CustomerDetails("Cust-1", "Test Name", "test@example.com", "1234567890", "Test Address"));
        order.setTotalPrice(100.0);

        // Create a new OrderItem object
        OrderItem item = new OrderItem();
        item.setOrderItemIdentifier(new OrderItemIdentifier());
        item.setProductId("test-product-id");
        item.setQuantity(2);
        item.setPrice(50.0);
        item.setOrder(order);

        // Add the OrderItem to the Order
        order.setItems(new HashSet<>(Arrays.asList(item)));

        // Save the Order to the repository
        orderRepository.save(order);
    }

    @Test
    public void testFindAll() {
        List<Order> orders = orderRepository.findAll();
        assertEquals(1, orders.size());
    }

    @Test
    public void whenOrderExists_testFindOrderByOrderIdentifier() {
        String validOrderId = orderRepository.findAll().get(0).getOrderIdentifier().getOrderId();

        Order foundOrder = orderRepository.findOrderByOrderIdentifier_OrderId(validOrderId)
                .orElse(null);

        assertNotNull(foundOrder);
        assertEquals(validOrderId, foundOrder.getOrderIdentifier().getOrderId());
    }

    @Test
    public void whenOrderDoesNotExist_testFindOrderByOrderIdentifier() {
        String invalidOrderId = "invalidId";

        Order foundOrder = orderRepository.findOrderByOrderIdentifier_OrderId(invalidOrderId)
                .orElse(null);

        assertNull(foundOrder);
    }

    @Test
    public void whenOrderExists_testDeleteByOrderIdentifier() {
        // Get an existing order
        Order existingOrder = orderRepository.findAll().get(0);
        String existingOrderId = existingOrder.getOrderIdentifier().getOrderId();

        // Delete the order
        orderRepository.deleteByOrderIdentifier_OrderId(existingOrderId);

        // Try to find the deleted order
        Order deletedOrder = orderRepository.findOrderByOrderIdentifier_OrderId(existingOrderId)
                .orElse(null);

        // Assert that the order was deleted
        assertNull(deletedOrder);
    }
}
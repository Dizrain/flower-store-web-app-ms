package com.example.ordersservice.datalayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();

        // Create and save an order
        Order order = new Order();
        order.setShippingAddress("123 Main St");
        order.setBillingInformation("Visa 1234");
        order.setStatus(OrderStatus.PLACED);
        order.setOrderIdentifier(new OrderIdentifier()); // Set OrderIdentifier
        order.setCustomerId(new CustomerIdentifier()); // Set CustomerIdentifier

        // Create an OrderItem and add it to the items list
        Order.OrderItem item = new Order.OrderItem();
        item.setProductId(new ProductIdentifier());
        item.setQuantity(1);
        order.setItems(List.of(item)); // Initialize items with the created OrderItem

        orderRepository.save(order);

        // Create and save another order
        Order anotherOrder = new Order();
        anotherOrder.setShippingAddress("456 Oak St");
        anotherOrder.setBillingInformation("MasterCard 5678");
        anotherOrder.setStatus(OrderStatus.SHIPPED);
        anotherOrder.setOrderIdentifier(new OrderIdentifier()); // Set OrderIdentifier
        anotherOrder.setCustomerId(new CustomerIdentifier()); // Set CustomerIdentifier

        // Create an OrderItem and add it to the items list
        Order.OrderItem anotherItem = new Order.OrderItem();
        anotherItem.setProductId(new ProductIdentifier());
        anotherItem.setQuantity(1);
        anotherOrder.setItems(List.of(anotherItem)); // Initialize items with the created OrderItem

        orderRepository.save(anotherOrder);
    }

    @Test
    public void testFindAll() {
        List<Order> orders = orderRepository.findAll();
        assertEquals(2, orders.size());
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
    public void testFindByCustomerId_CustomerId() {
        String validCustomerId = orderRepository.findAll().get(0).getCustomerId().getCustomerId();

        List<Order> foundOrders = orderRepository.findByCustomerId_CustomerId(validCustomerId);

        assertFalse(foundOrders.isEmpty());
        assertEquals(validCustomerId, foundOrders.get(0).getCustomerId().getCustomerId());
    }

    @Test
    public void whenNoOrdersForCustomer_testFindByCustomerId_CustomerId() {
        String invalidCustomerId = "invalidId";

        List<Order> foundOrders = orderRepository.findByCustomerId_CustomerId(invalidCustomerId);

        assertTrue(foundOrders.isEmpty());
    }

    @Test
    public void testFindByItems_ProductId_ProductId() {
        ProductIdentifier validProductId = orderRepository.findAll().get(0).getItems().get(0).getProductId();

        List<Order> foundOrders = orderRepository.findByItems_ProductId_ProductId(validProductId.getProductId());

        assertFalse(foundOrders.isEmpty());
        assertEquals(validProductId, foundOrders.get(0).getItems().get(0).getProductId());
    }

    @Test
    public void whenNoOrdersForProduct_testFindByItems_ProductId_ProductId() {
        ProductIdentifier invalidProductId = new ProductIdentifier("invalidId");

        List<Order> foundOrders = orderRepository.findByItems_ProductId_ProductId(invalidProductId.getProductId());

        assertTrue(foundOrders.isEmpty());
    }
}
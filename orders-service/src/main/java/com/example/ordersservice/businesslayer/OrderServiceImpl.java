package com.example.ordersservice.businesslayer;

import com.example.ordersservice.datalayer.*;
import com.example.ordersservice.datamapperlayer.OrderRequestMapper;
import com.example.ordersservice.datamapperlayer.OrderResponseMapper;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentRequestModel;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentResponseModel;
import com.example.ordersservice.datamapperlayer.productdtos.StockItemRequestModel;
import com.example.ordersservice.datamapperlayer.productdtos.StockItemResponseModel;
import com.example.ordersservice.domainclientlayer.PaymentsServiceClient;
import com.example.ordersservice.domainclientlayer.ProductsServiceClient;
import com.example.ordersservice.presentationlayer.OrderItemResponseModel;
import com.example.ordersservice.utils.exceptions.NotFoundException;
import com.example.ordersservice.utils.exceptions.OutOfStockException;
import com.example.ordersservice.presentationlayer.OrderRequestModel;
import com.example.ordersservice.presentationlayer.OrderResponseModel;
import com.example.ordersservice.presentationlayer.OrderItemUpdateRequestModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.UID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductsServiceClient productsServiceClient;
    private final PaymentsServiceClient paymentsServiceClient;
    private final OrderRequestMapper orderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;

    OrderServiceImpl(OrderRepository orderRepository,
                     ProductsServiceClient productsServiceClient,
                     PaymentsServiceClient paymentsServiceClient,
                     OrderRequestMapper orderRequestMapper,
                     OrderResponseMapper orderResponseMapper) {
        this.orderRepository = orderRepository;
        this.productsServiceClient = productsServiceClient;
        this.paymentsServiceClient = paymentsServiceClient;
        this.orderRequestMapper = orderRequestMapper;
        this.orderResponseMapper = orderResponseMapper;
    }

    @Override
    public OrderResponseModel getOrder(String orderIdentifier) {
        Order order = orderRepository.findOrderByOrderIdentifier_OrderId(orderIdentifier)
                .orElseThrow(() -> new NotFoundException("Order not found."));
        return orderResponseMapper.entityToResponseModel(order);
    }

    // In OrderServiceImpl.java
    @Override
    @Transactional
    public OrderResponseModel createOrder(OrderRequestModel requestModel) {
        Order order = orderRequestMapper.requestModelToEntity(requestModel, new OrderIdentifier());
        Set<OrderItem> items = requestModel.getItems().stream()
                .map(itemRequest -> {
                    OrderItem orderItem = orderRequestMapper.mapItem(itemRequest, new OrderItemIdentifier());
                    orderItem.setOrder(order);  // Set the order in the OrderItem
                    return orderItem;
                })
                .collect(Collectors.toSet());
        order.setItems(items);

        validateProductAvailability(order);

        // Decrease stock level for each item in the order
        order.getItems().forEach(item -> {
            item.setOrderItemIdentifier(new OrderItemIdentifier());

            StockItemResponseModel stockItem = productsServiceClient.getStockItemByProductId(item.getProductId());
            int newStockLevel = stockItem.getStockLevel() - item.getQuantity();
            StockItemRequestModel stockItemRequest = StockItemRequestModel.builder()
                    .productId(item.getProductId())
                    .stockLevel(newStockLevel)
                    .reorderThreshold(stockItem.getReorderThreshold())
                    .build();
            productsServiceClient.updateStockItem(stockItemRequest);
        });

        double totalPrice = calculateTotalPrice(order);
        order.setTotalPrice(totalPrice);

        PaymentRequestModel paymentRequest = PaymentRequestModel.builder()
                .paymentId(new UID().toString())
                .amount(totalPrice)
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CreditCard) // Hardcoded for now
                .build();
        PaymentResponseModel paymentResponse = paymentsServiceClient.processPayment(paymentRequest);

        order.setStatus(OrderStatus.PLACED);
        Order savedOrder = orderRepository.save(order);
        return orderResponseMapper.entityToResponseModel(savedOrder);
    }

    @Override
    @Transactional
    public OrderItemResponseModel updateOrderItem(String orderIdentifier, String orderItemIdentifier, OrderItemUpdateRequestModel itemUpdate) {
        Order order = orderRepository.findOrderByOrderIdentifier_OrderId(orderIdentifier)
                .orElseThrow(() -> new NotFoundException("Order not found."));
        OrderItem orderItem = order.getItems().stream()
                .filter(item -> item.getOrderItemIdentifier().getOrderItemId().equals(orderItemIdentifier))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found."));
        if (!productsServiceClient.isProductAvailable(itemUpdate.getProductId(), itemUpdate.getQuantity())) {
            throw new OutOfStockException("Product is out of stock.");
        }
        int oldQuantity = orderItem.getQuantity();
        orderRequestMapper.updateEntity(itemUpdate, orderItem);

        // Update stock level
        StockItemResponseModel stockItem = productsServiceClient.getStockItemByProductId(orderItem.getProductId());
        int newStockLevel = stockItem.getStockLevel() - (itemUpdate.getQuantity() - oldQuantity); // Adjust the stock level
        StockItemRequestModel stockItemRequest = StockItemRequestModel.builder()
                .productId(orderItem.getProductId())
                .stockLevel(newStockLevel)
                .reorderThreshold(stockItem.getReorderThreshold())
                .build();
        productsServiceClient.updateStockItem(stockItemRequest);

        // Update total price
        double totalPrice = calculateTotalPrice(order);
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        return orderResponseMapper.entityToResponseModel(orderItem);
    }

    @Override
    public List<OrderItemResponseModel> getAllOrderItems(String orderIdentifier) {
        Order order = orderRepository.findOrderByOrderIdentifier_OrderId(orderIdentifier)
                .orElseThrow(() -> new NotFoundException("Order not found."));
        return order.getItems().stream()
                .map(orderResponseMapper::entityToResponseModel)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseModel getOrderItem(String orderIdentifier, String orderItemIdentifier) {
        Order order = orderRepository.findOrderByOrderIdentifier_OrderId(orderIdentifier)
                .orElseThrow(() -> new NotFoundException("Order not found."));
        OrderItem orderItem = order.getItems().stream()
                .filter(item -> item.getOrderItemIdentifier().getOrderItemId().equals(orderItemIdentifier))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Order item not found."));
        return orderResponseMapper.entityToResponseModel(orderItem);
    }

    @Override
    @Transactional
    public void deleteOrder(String orderIdentifier) {
        orderRepository.deleteByOrderIdentifier_OrderId(orderIdentifier);
    }

    @Override
    public List<OrderResponseModel> getOrdersByCustomer(CustomerIdentifier customerIdentifier) {
        List<Order> orders = orderRepository.findOrdersByCustomerDetails_CustomerId(customerIdentifier.getCustomerId());
        return orders.stream()
                .map(orderResponseMapper::entityToResponseModel)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponseModel updateOrderStatus(String orderIdentifier, OrderStatus newStatus) {
       Order order = orderRepository.findOrderByOrderIdentifier_OrderId(orderIdentifier)
                .orElseThrow(() -> new NotFoundException("Order not found."));
        if (!canUpdateStatus(order, newStatus)) {
            throw new IllegalStateException("Update to the requested status is not allowed.");
        }
        order.setStatus(newStatus);
        orderRepository.save(order);
        return orderResponseMapper.entityToResponseModel(order);
    }

    private void validateProductAvailability(Order order) {
        List<String> unavailableProductIds = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            if (!productsServiceClient.isProductAvailable(item.getProductId(), item.getQuantity())) {
                unavailableProductIds.add(item.getProductId());
            }
        }
        if (!unavailableProductIds.isEmpty()) {
            throw new OutOfStockException("Products with IDs " + String.join(", ", unavailableProductIds) + " are not available.");
        }
    }

    private double calculateTotalPrice(Order order) {
        return order.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }

    private boolean canUpdateStatus(Order order, OrderStatus newStatus) {
        // Example: Cannot change status back to PLACED from any other state.
        return !(order.getStatus().compareTo(newStatus) > 0);
    }
}

package com.example.ordersservice.businesslayer;

import com.example.ordersservice.datalayer.*;
import com.example.ordersservice.datamapperlayer.OrderRequestMapper;
import com.example.ordersservice.datamapperlayer.OrderResponseMapper;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentRequestModel;
import com.example.ordersservice.datamapperlayer.paymentdtos.PaymentResponseModel;
import com.example.ordersservice.datamapperlayer.productdtos.ProductResponseModel;
import com.example.ordersservice.datamapperlayer.productdtos.StockItemRequestModel;
import com.example.ordersservice.datamapperlayer.productdtos.StockItemResponseModel;
import com.example.ordersservice.domainclientlayer.PaymentsServiceClient;
import com.example.ordersservice.domainclientlayer.ProductsServiceClient;
import com.example.ordersservice.presentationlayer.OrderRequestModel;
import com.example.ordersservice.presentationlayer.OrderResponseModel;
import com.example.ordersservice.utils.exceptions.NotFoundException;
import com.example.ordersservice.utils.exceptions.OutOfStockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRequestMapper orderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;
    private final PaymentsServiceClient paymentsServiceClient;
    private final ProductsServiceClient productsServiceClient;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderRequestMapper orderRequestMapper,
                            OrderResponseMapper orderResponseMapper,
                            PaymentsServiceClient paymentsServiceClient,
                            ProductsServiceClient productsServiceClient) {
        this.orderRepository = orderRepository;
        this.orderRequestMapper = orderRequestMapper;
        this.orderResponseMapper = orderResponseMapper;
        this.paymentsServiceClient = paymentsServiceClient;
        this.productsServiceClient = productsServiceClient;
    }

    @Override
    public List<OrderResponseModel> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderResponseMapper.entityListToResponseModelList(orders);
    }

    @Override
    public OrderResponseModel getOrderById(String orderId) {
        Order order = orderRepository.findOrderByOrderIdentifier_OrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id " + orderId));
        return orderResponseMapper.entityToResponseModel(order);
    }

    @Override
    @Transactional
    public OrderResponseModel createOrder(OrderRequestModel orderRequestModel) {
        // Check stock level for each product in the order
        for (OrderRequestModel.OrderItemModel item : orderRequestModel.getItems()) {
            StockItemResponseModel stockItem = productsServiceClient.getStockItemByProductId(item.getProductId());
            if (stockItem.getStockLevel() < item.getQuantity()) {
                throw new OutOfStockException("Product with id " + item.getProductId() + " is out of stock.");
            }
        }

        // Process payment
        PaymentRequestModel paymentRequestModel = PaymentRequestModel.builder()
                .amount(calculateOrderTotal(orderRequestModel))
                .paymentDate(LocalDateTime.now())
                .paymentMethod(PaymentMethod.CreditCard)
                .build();
        PaymentResponseModel paymentResponseModel = paymentsServiceClient.processPayment(paymentRequestModel);

        // Create order
        Order order = orderRequestMapper.requestModelToEntity(orderRequestModel, new OrderIdentifier());
        order.setStatus(OrderStatus.PLACED); // Set initial status
        order.setPaymentId(new PaymentIdentifier(paymentResponseModel.getPaymentId())); // Set payment id
        order.setCustomerId(new CustomerIdentifier(orderRequestModel.getCustomerId()));

        // Map items and add product id to each item
        List<Order.OrderItem> orderItems = orderRequestModel.getItems().stream()
                .map(item -> {
                    Order.OrderItem orderItem =   orderRequestMapper.orderItemModelToOrderItem(item);
                    orderItem.setProductId(orderRequestMapper.productIdentifierFromOrderItemModel(item));
                    return orderItem;
                })
                .toList();

        order.setItems(orderItems);

        // Update stock level for each product in the order
        for (OrderRequestModel.OrderItemModel item : orderRequestModel.getItems()) {
            StockItemRequestModel stockItemRequestModel = StockItemRequestModel.builder()
                    .productId(item.getProductId())
                    .stockLevel(-item.getQuantity())
                    .build();
            productsServiceClient.updateStockItem(stockItemRequestModel);
        }

        Order savedOrder = orderRepository.save(order);

        OrderResponseModel orderResponseModel = orderResponseMapper.entityToResponseModel(savedOrder);

        return orderResponseModel;
    }

    @Override
    @Transactional
    public OrderResponseModel updateOrder(OrderRequestModel updatedOrderModel, String orderId) {
        Order foundOrder = orderRepository.findOrderByOrderIdentifier_OrderId(orderId).orElseThrow(() -> new NotFoundException("Order not found with id " + orderId));
        Order updatedOrder = orderRequestMapper.requestModelToEntity(updatedOrderModel, foundOrder.getOrderIdentifier());
        updatedOrder.setId(foundOrder.getId());
        updatedOrder.setOrderIdentifier(foundOrder.getOrderIdentifier());
        updatedOrder.setStatus(foundOrder.getStatus());
        updatedOrder.setItems(foundOrder.getItems());
        updatedOrder.setPaymentId(foundOrder.getPaymentId());
        updatedOrder.setCustomerId(foundOrder.getCustomerId());
        Order savedOrder = orderRepository.save(updatedOrder);
        return orderResponseMapper.entityToResponseModel(savedOrder);
    }

    @Override
    public void cancelOrder(String orderId) {
        Order order = orderRepository.findOrderByOrderIdentifier_OrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found with id: " + orderId));
        order.setStatus(OrderStatus.CANCELLED); // Update status instead of deleting
        orderRepository.save(order); // Save the updated order
    }

    @Override
    public List<OrderResponseModel> getOrdersByCustomerId(String customerId) {
        List<Order> orders = orderRepository.findByCustomerId_CustomerId(customerId);
        return orderResponseMapper.entityListToResponseModelList(orders);
    }

    @Override
    public List<OrderResponseModel> getOrdersByProductId(String productId) {
        List<Order> orders = orderRepository.findByItems_ProductId_ProductId(productId);
        return orderResponseMapper.entityListToResponseModelList(orders);
    }

    private double calculateOrderTotal(OrderRequestModel orderRequestModel) {
        return orderRequestModel.getItems().stream()
                .mapToDouble(item -> {
                    String productId = item.getProductId();
                    ProductResponseModel product = productsServiceClient.getProductByProductId(productId);
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
    }
}

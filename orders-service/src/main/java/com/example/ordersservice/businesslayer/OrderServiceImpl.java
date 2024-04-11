package com.example.ordersservice.businesslayer;

import com.example.ordersservice.datalayer.Order;
import com.example.ordersservice.datalayer.OrderIdentifier;
import com.example.ordersservice.datalayer.OrderRepository;
import com.example.ordersservice.datalayer.OrderStatus;
import com.example.ordersservice.datamapperlayer.OrderRequestMapper;
import com.example.ordersservice.datamapperlayer.OrderResponseMapper;
import com.example.ordersservice.presentationlayer.OrderRequestModel;
import com.example.ordersservice.presentationlayer.OrderResponseModel;
import com.example.ordersservice.utils.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderRequestMapper orderRequestMapper;
    private final OrderResponseMapper orderResponseMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderRequestMapper orderRequestMapper,
                            OrderResponseMapper orderResponseMapper) {
        this.orderRepository = orderRepository;
        this.orderRequestMapper = orderRequestMapper;
        this.orderResponseMapper = orderResponseMapper;
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
    public OrderResponseModel createOrder(OrderRequestModel orderRequestModel) {
        Order order = orderRequestMapper.requestModelToEntity(orderRequestModel, new OrderIdentifier());
        order.setStatus(OrderStatus.PLACED); // Set initial status
        Order savedOrder = orderRepository.save(order);
        return orderResponseMapper.entityToResponseModel(savedOrder);
    }

    @Override
    public OrderResponseModel updateOrder(OrderRequestModel updatedOrderModel, String orderId) {
        Order foundOrder = orderRepository.findById(orderId).orElseThrow(() -> new NotFoundException("Order not found with id " + orderId));
        // Here, we could apply updates to the foundOrder from updatedOrderModel, including ensuring it has at least one order item
        Order updatedOrder = orderRequestMapper.requestModelToEntity(updatedOrderModel, foundOrder.getOrderIdentifier());
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
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orderResponseMapper.entityListToResponseModelList(orders);
    }
}

package com.example.ordersservice.businesslayer;

import com.example.ordersservice.datalayer.Order;
import com.example.ordersservice.presentationlayer.OrderRequestModel;
import com.example.ordersservice.presentationlayer.OrderResponseModel;

import java.util.List;

public interface OrderService {

    /**
     * Retrieves all customer orders.
     * @return a list of OrderResponseModel representing all orders.
     */
    List<OrderResponseModel> getAllOrders();

    /**
     * Retrieves a single order by its ID.
     * @param orderId the unique identifier of the order.
     * @return the OrderResponseModel of the requested order.
     */
    OrderResponseModel getOrderById(String orderId);

    /**
     * Creates a new order.
     * @param orderRequestModel the order information to be created.
     * @return the OrderResponseModel of the newly created order.
     */
    OrderResponseModel createOrder(OrderRequestModel orderRequestModel);

    /**
     * Updates an existing order.
     * @param updatedOrder the updated order information.
     * @param orderId the ID of the order to update.
     * @return the OrderResponseModel of the updated order.
     */
    OrderResponseModel updateOrder(OrderRequestModel updatedOrder, String orderId);

    /**
     * Cancels an existing order.
     * @param orderId the ID of the order to cancel.
     */
    void cancelOrder(String orderId);

    /**
     * Retrieves orders by customer ID.
     * @param customerId the ID of the customer whose orders are to be retrieved.
     * @return a list of OrderResponseModel for the specified customer.
     */
    List<OrderResponseModel> getOrdersByCustomerId(String customerId);
}

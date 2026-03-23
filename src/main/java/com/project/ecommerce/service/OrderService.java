package com.project.ecommerce.service;

import java.util.List;
import java.util.UUID;

import com.project.ecommerce.dto.order.OrderDetailsDto;
import com.project.ecommerce.dto.order.OrderItemResponseDto;
import com.project.ecommerce.dto.order.OrderRequestDto;
import com.project.ecommerce.dto.order.OrderResponseDto;
import com.project.ecommerce.model.Enums.OrderStatus;

public interface OrderService {

    /**
     * Place order for a customer
     * 
     * @param requestDto contains address of customer and payment method
     * @param cart       contains cartitems of a customer in a cart
     * @return OrderResponseDto which contains order details
     */
    OrderResponseDto placeOrder(OrderRequestDto requestDto, UUID userId);

    /**
     * Get my orders
     * 
     * @param userId id of user
     * @return List of orders of the given user
     */
    List<OrderDetailsDto> getMyOrders(UUID userId);

    /**
     * Retrieves detailed information of a specific order for a user.
     * <p>
     * Includes order items, pricing, shipping address and current status.
     *
     * @param orderId unique identifier of the order
     * @param userId  unique identifier of the user
     * @return OrderDetailsDto containing full order details
     */
    List<OrderItemResponseDto> getOrderDetails(UUID userId, UUID orderId);

    /**
     * Cancels an order placed by a user.
     * <p>
     * Cancellation is allowed only if the order has not been shipped.
     * Product stock will be restored after successful cancellation.
     *
     * @param orderId unique identifier of the order
     * @param userId  unique identifier of the user
     */
    void cancelOrder(UUID orderId, UUID userId);

    /**
     * Retrieves all orders in the system (admin use only).
     *
     * @return list of all orders
     */
    List<OrderDetailsDto> getAllOrders();

    /**
     * Updates the status of an order (admin use only).
     *
     * @param orderId unique identifier of the order
     * @param status  new status to be applied to the order
     */
    void updateOrderStatus(UUID orderId, OrderStatus status);

    // /**
    //  * Retrieves orders filtered by their status (admin use only).
    //  *
    //  * @param status order status to filter by
    //  * @return list of orders matching the given status
    //  */
    // List<OrderResponseDto> getOrdersByStatus(OrderStatus status);
}
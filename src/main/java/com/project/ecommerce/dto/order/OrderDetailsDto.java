package com.project.ecommerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.project.ecommerce.model.Order;
import com.project.ecommerce.model.Enums.OrderStatus;
import com.project.ecommerce.model.Enums.PaymentMethod;
import com.project.ecommerce.model.Enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailsDto {
    private UUID id;
    private UUID userId;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private BigDecimal grandTotal;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;

    public static OrderDetailsDto from(Order order) {
        return new OrderDetailsDto(order.getId(), order.getUser().getId(), order.getOrderStatus(), order.getPaymentMethod(), order.getPaymentStatus(), order.getGrandTotal(), order.getCreatedAt(), order.getDeliveredAt());
    }
}

package com.project.ecommerce.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.project.ecommerce.model.Enums.OrderStatus;
import com.project.ecommerce.model.Enums.PaymentMethod;
import com.project.ecommerce.model.Enums.PaymentStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderResponseDto {
    
    private UUID orderId;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private BigDecimal totalAmount;
    private BigDecimal shippingCost;
    private BigDecimal grandTotal;

    private UUID addressId;
    private LocalDateTime createdAt;
    
    private List<OrderItemResponseDto> items;

}

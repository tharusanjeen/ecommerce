package com.project.ecommerce.dto.order;

import com.project.ecommerce.model.Enums.OrderStatus;

import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}

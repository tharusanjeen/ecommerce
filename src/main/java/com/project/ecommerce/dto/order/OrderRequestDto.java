package com.project.ecommerce.dto.order;

import java.util.UUID;

import com.project.ecommerce.model.Enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {
    
    @NotNull
    private UUID addressId;

    @NotNull
    private PaymentMethod paymentMethod;
}

package com.projectone.ecommerce.dtos;

import java.util.UUID;

import com.projectone.ecommerce.model.User;

public record UserDto (UUID id, String username) {
    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
}

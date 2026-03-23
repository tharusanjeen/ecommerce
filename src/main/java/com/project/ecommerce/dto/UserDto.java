package com.project.ecommerce.dto;

import java.util.UUID;

import com.project.ecommerce.model.User;
import com.project.ecommerce.model.Enums.Role;

public record UserDto(UUID id, String username, String email, Role role) {

    public static UserDto from(User user) {
        return new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }    
}

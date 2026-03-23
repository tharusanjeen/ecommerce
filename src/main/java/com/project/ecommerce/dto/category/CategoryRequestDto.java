package com.project.ecommerce.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDto {

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 100, message = "Description must be between 3 to 20 characters.")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    @Size(min = 30, max = 300, message = "Description must be between 30 to 300 characters.")
    private String description;
}

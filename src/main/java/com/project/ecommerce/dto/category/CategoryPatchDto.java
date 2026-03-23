package com.project.ecommerce.dto.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryPatchDto {

    @Size(min = 3, max = 100, message = "Description must be between 3 to 20 characters.")
    private String name;

    @Size(min = 60, max = 300, message = "Description must be between 60 to 300 characters.")
    private String description;
    
    private Boolean isActive;
}

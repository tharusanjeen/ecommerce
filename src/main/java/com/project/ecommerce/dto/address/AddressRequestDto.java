package com.project.ecommerce.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequestDto {

    @NotBlank(message = "Full name is required!")
    @Size(min = 4, max = 50, message = "Full name must be between 4 and 50 characters.")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Full name must contain only letters and spaces.")
    private String fullName;

    @NotBlank(message = "Phone is required!")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits.")
    private String phone;

    @NotBlank(message = "Province is required!")
    @Size(min = 2, max = 50, message = "Province name must be between 2 and 50 characters.")
    private String province;

    @NotBlank(message = "District is required!")
    @Size(min = 2, max = 50, message = "District name must be between 2 and 50 characters.")
    private String district;

    @NotBlank(message = "City is required!")
    @Size(min = 2, max = 50, message = "City name must be between 2 and 50 characters.")
    private String city;

    @NotBlank(message = "Street/Tole is required!")
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters.")
    private String street;

    @Size(max = 100, message = "Landmark must be at most 100 characters.")
    private String landmark;
}

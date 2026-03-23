package com.project.ecommerce.dto.address;

import java.util.UUID;

import com.project.ecommerce.model.Address;

public record AddressResponseDto(UUID id, UUID userId, String fullName, String phone, String province, String district, String city, String street, boolean isDefault) {
    
    public static AddressResponseDto from(Address address) {
        return new AddressResponseDto(address.getId(), address.getUser().getId(), address.getFullName(), address.getPhone(), address.getProvince(), address.getDistrict(), address.getCity(), address.getStreet(), address.isDefault());
    }
}

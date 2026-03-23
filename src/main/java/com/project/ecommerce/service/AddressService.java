package com.project.ecommerce.service;


import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.project.ecommerce.dto.address.AddressRequestDto;
import com.project.ecommerce.dto.address.AddressResponseDto;

/**
 * Service interface for managing user addresses.
 * Provides methods to add, fetch, update, and remove addresses
 * as well as retrieving the default address for checkout purposes.
 */
public interface AddressService {

    /**
     * Retrieves all addresses for a given user.
     *
     * @param userId the UUID of the user
     * @return a list of AddressResponseDto representing all user addresses
     */
    List<AddressResponseDto> getAllAddresses(UUID userId);

    /**
     * Retrieves the default address for a given user.
     * This is typically used as the shipping address during checkout.
     *
     * @param userId the UUID of the user
     * @return the default AddressResponseDto
     */
    AddressResponseDto getDefaultAddress(UUID userId);

    /**
     * Adds a new address for a given user.
     *
     * @param userId the UUID of the user
     * @param requestDto the address details to add
     * @return the AddressResponseDto of the newly added address
     */
    AddressResponseDto addAddress(UUID userId, AddressRequestDto requestDto);

    /**
     * Updates an existing address for a user.
     *
     * @param userId the UUID of the user
     * @param addressId the UUID of the address to update
     * @param requestDto the updated address details
     * @return the AddressResponseDto of the updated address
     */
    AddressResponseDto updateAddress(UUID userId, UUID addressId, AddressRequestDto requestDto) throws AccessDeniedException;

    /**
     * Removes an existing address for a user.
     *
     * @param userId the UUID of the user
     * @param addressId the UUID of the address to remove
     * @return a map containing operation status and optional message
     */
    Map<String, Object> removeAddress(UUID userId, UUID addressId) throws AccessDeniedException;

     /**
     * Changes default address.
     *
     * @param userId the UUID of the user
     * @param addressId the UUID of the address to remove
     * @return a map containing operation status and optional message
     */
    AddressResponseDto updateDefault(UUID userId, UUID addressId) throws AccessDeniedException;


}

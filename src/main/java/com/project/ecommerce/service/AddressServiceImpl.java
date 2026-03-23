package com.project.ecommerce.service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.ecommerce.dto.address.AddressRequestDto;
import com.project.ecommerce.dto.address.AddressResponseDto;
import com.project.ecommerce.exception.CustomExceptions.AddressNotFoundException;
import com.project.ecommerce.model.Address;
import com.project.ecommerce.model.User;
import com.project.ecommerce.repository.AddressRepository;
import com.project.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<AddressResponseDto> getAllAddresses(UUID userId) {
        User user = getUser(userId);

        List<Address> addresses = addressRepository.findByUser(user);

        return addresses.stream().map(AddressResponseDto::from).collect(Collectors.toList());
    }

    @Override
    public AddressResponseDto getDefaultAddress(UUID userId) {
        User user = getUser(userId);

        Address defaultAddress = addressRepository.findByUserAndIsDefaultTrue(user).orElseThrow(() -> new AddressNotFoundException("Default address not found!"));

        return AddressResponseDto.from(defaultAddress);
    }

    @Override
    public AddressResponseDto addAddress(UUID userId, AddressRequestDto requestDto) {

        User user = getUser(userId);

        int addressCount = addressRepository.countByUser(user);

        if(addressCount >= 3) {
            throw new IllegalStateException("Cannot add more than 3 addresses for a user.");
        }

        Address address = Address.builder()
        .fullName(requestDto.getFullName())
        .user(user)
        .phone(requestDto.getPhone())
        .province(requestDto.getProvince())
        .district(requestDto.getDistrict())
        .city(requestDto.getCity())
        .street(requestDto.getStreet())
        .build();

        if(addressCount == 0) {
            address.setDefault(true);
        }

        return AddressResponseDto.from(addressRepository.save(address));
    }

    @Override
    public AddressResponseDto updateAddress(UUID userId, UUID addressId, AddressRequestDto requestDto) throws AccessDeniedException {

        User user = getUser(userId);

        Address address = getAddress(addressId);

        if(!address.getUser().equals(user)) throw new AccessDeniedException("You cannot update this address!");

        address.setFullName(requestDto.getFullName());
        address.setPhone(requestDto.getPhone());
        address.setProvince(requestDto.getProvince());
        address.setDistrict(requestDto.getDistrict());
        address.setCity(requestDto.getCity());
        address.setStreet(requestDto.getStreet());

        return AddressResponseDto.from(addressRepository.save(address));
    }

    @Override
    public Map<String, Object> removeAddress(UUID userId, UUID addressId) throws AccessDeniedException {

        User user = getUser(userId);

        Address address = getAddress(addressId);

        if(!address.getUser().equals(user)) throw new AccessDeniedException("You cannot remove this address!");

        boolean wasDefault = address.isDefault();
        
        addressRepository.delete(address);

        if (wasDefault) {
            Optional<Address> another = addressRepository.findFirstByUser(user);
            another.ifPresent(newDef -> {
                newDef.setDefault(true);
                addressRepository.save(newDef);
            });
        }

        return Map.of("deleted", true);
    }

    @Override
    public AddressResponseDto updateDefault(UUID userId, UUID addressId) throws AccessDeniedException {
        User user = getUser(userId);

        Address address = getAddress(addressId);

        if(!address.getUser().equals(user)) throw new AccessDeniedException("You cannot update this address");

        for(Address oldDefault : user.getAddresses()) {

            if(oldDefault.isDefault()) {
                oldDefault.setDefault(false);
                addressRepository.save(oldDefault);
            }
        }

        address.setDefault(true);

        return AddressResponseDto.from(addressRepository.save(address));
    }

    // Helper methods
    private User getUser(UUID userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    private Address getAddress(UUID addressId) {
        return addressRepository.findById(addressId).orElseThrow(() -> new AddressNotFoundException("Address not found!"));
    }

}

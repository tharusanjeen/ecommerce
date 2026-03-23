package com.project.ecommerce.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.ecommerce.dto.address.AddressRequestDto;
import com.project.ecommerce.dto.address.AddressResponseDto;
import com.project.ecommerce.model.User;
import com.project.ecommerce.service.AddressServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
@PreAuthorize("hasRole('USER')")
public class AddressController {
    
    private final AddressServiceImpl addressServiceImpl;

    @PostMapping("/add")
    public ResponseEntity<AddressResponseDto> addAddress(Authentication auth, @Valid @RequestBody AddressRequestDto requestDto) {
        User user = (User) auth.getPrincipal();

        AddressResponseDto response = addressServiceImpl.addAddress(user.getId(), requestDto);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AddressResponseDto>> getAllAddresses(Authentication auth) {
        User user = (User) auth.getPrincipal();

        List<AddressResponseDto> response = addressServiceImpl.getAllAddresses(user.getId());

        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/default")
    public ResponseEntity<AddressResponseDto> getDefaultAddresses(Authentication auth) {
        User user = (User) auth.getPrincipal();

        AddressResponseDto response = addressServiceImpl.getDefaultAddress(user.getId());

        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<AddressResponseDto> updateAddress(Authentication auth, @PathVariable UUID id, @Valid @RequestBody AddressRequestDto requestDto) throws AccessDeniedException {
        User user = (User) auth.getPrincipal();

        return ResponseEntity.ok().body(addressServiceImpl.updateAddress(user.getId(), id, requestDto));
    }

    @PatchMapping("/{id}/default")
    public ResponseEntity<AddressResponseDto> updateDefault(Authentication auth, @PathVariable UUID id) throws AccessDeniedException {
        User user = (User) auth.getPrincipal();

        return ResponseEntity.ok().body(addressServiceImpl.updateDefault(user.getId(), id));
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Map<String, Object>> removeAddress(Authentication auth, @PathVariable UUID id) throws AccessDeniedException {

        User user = (User) auth.getPrincipal();

        return ResponseEntity.ok().body(addressServiceImpl.removeAddress(user.getId(), id));
    }
}

package com.project.ecommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.ecommerce.model.Address;
import com.project.ecommerce.model.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findByUser(User user);
    Optional<Address> findByUserAndIsDefaultTrue(User user);
    Optional<Address> findFirstByUser(User user);
    int countByUser(User user); 
}

package com.team4.secureit.repository;

import com.team4.secureit.model.PropertyOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PropertyOwnerRepository extends JpaRepository<PropertyOwner, UUID> {
    Optional<PropertyOwner> findByEmail(String userEmail);
}

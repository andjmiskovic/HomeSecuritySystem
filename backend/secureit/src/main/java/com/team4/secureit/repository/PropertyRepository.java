package com.team4.secureit.repository;

import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    @Query("SELECT p FROM Property p WHERE (:search IS NULL OR LOWER(p.name) LIKE %:search% OR LOWER(p.address) LIKE %:search%) AND (:type IS NULL OR p.type = :type)")
    List<Property> getAll(@Param("search") String search, @Param("type") PropertyType type);
}

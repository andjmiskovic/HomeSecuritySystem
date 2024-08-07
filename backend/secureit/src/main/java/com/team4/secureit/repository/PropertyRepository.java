package com.team4.secureit.repository;

import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.PropertyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PropertyRepository extends JpaRepository<Property, UUID> {

    @Query("SELECT p FROM Property p JOIN PropertyOwner o ON p.owner.id = o.id WHERE (:search IS NULL OR LOWER(p.name) LIKE %:search% OR LOWER(p.address) LIKE %:search% OR CAST(p.id AS string) LIKE %:search% OR LOWER(CONCAT(o.firstName, ' ', o.lastName)) LIKE %:search%) AND (:type IS NULL OR p.type = :type)")
    List<Property> getAll(@Param("search") String search, @Param("type") PropertyType type);

    @Query("SELECT p FROM Property p JOIN PropertyOwner o ON p.owner.id = o.id WHERE (p.owner.id = :userId) AND (:search IS NULL OR LOWER(p.name) LIKE %:search% OR LOWER(p.address) LIKE %:search% OR CAST(p.id AS string) LIKE %:search% OR LOWER(CONCAT(o.firstName, ' ', o.lastName)) LIKE %:search%) AND (:type IS NULL OR p.type = :type)")
    List<Property> getPropertiesWhereUserIsOwner(@Param("userId") UUID userId, @Param("search") String search, @Param("type") PropertyType type);

    @Query("SELECT DISTINCT p FROM Property p JOIN PropertyOwner o ON p.owner.id = o.id JOIN p.tenants t WHERE (t.id = :userId) AND (:search IS NULL OR LOWER(p.name) LIKE %:search% OR LOWER(p.address) LIKE %:search% OR CAST(p.id AS string) LIKE %:search% OR LOWER(CONCAT(o.firstName, ' ', o.lastName)) LIKE %:search%) AND (:type IS NULL OR p.type = :type)")
    List<Property> getPropertiesWhereUserIsTenant(@Param("userId") UUID userId, @Param("search") String search, @Param("type") PropertyType type);

    Optional<Property> getPropertyByIdAndOwner(UUID propertyId, PropertyOwner propertyOwner);
}

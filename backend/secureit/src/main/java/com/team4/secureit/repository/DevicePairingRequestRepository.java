package com.team4.secureit.repository;

import com.team4.secureit.model.DevicePairingRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

public interface DevicePairingRequestRepository extends JpaRepository<DevicePairingRequest, UUID> {
    Boolean existsByCodeAndCreatedAfter(String code, Instant createdAfter);

    default Boolean existsByCode(String code) {
        return existsByCodeAndCreatedAfter(code, Instant.now().minus(5, ChronoUnit.MINUTES));
    }

    Optional<DevicePairingRequest> getByCodeAndCreatedAfter(String code, Instant createdAfter);

    default Optional<DevicePairingRequest> getByCode(String code) {
        return getByCodeAndCreatedAfter(code, Instant.now().minus(5, ChronoUnit.MINUTES));
    }
}

package com.team4.secureit.repository;

import com.team4.secureit.model.PersistedCSR;
import com.team4.secureit.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PersistedCSRRepository extends JpaRepository<PersistedCSR, UUID> {
    List<PersistedCSR> findByStatus(RequestStatus status);
}
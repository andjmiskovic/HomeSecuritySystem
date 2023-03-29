package com.team4.secureit.repository;

import com.team4.secureit.model.CertificateSigningRequest;
import com.team4.secureit.model.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CSRRepository extends JpaRepository<CertificateSigningRequest, UUID> {
    List<CertificateSigningRequest> findByStatus(RequestStatus status);
}
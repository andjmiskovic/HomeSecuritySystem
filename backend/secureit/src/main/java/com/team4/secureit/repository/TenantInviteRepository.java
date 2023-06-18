package com.team4.secureit.repository;

import com.team4.secureit.model.TenantInvite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TenantInviteRepository extends JpaRepository<TenantInvite, UUID> {
    Optional<TenantInvite> findByVerificationCode(String verificationCode);
}

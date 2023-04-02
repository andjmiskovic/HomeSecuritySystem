package com.team4.secureit.repository;

import com.team4.secureit.model.CertificateDetails;
import com.team4.secureit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateDetailsRepository extends JpaRepository<CertificateDetails, BigInteger> {
    Optional<CertificateDetails> findByAlias(String alias);

    List<CertificateDetails> findBySubscriber(User user);

    Optional<CertificateDetails> findBySerialNumber(BigInteger serialNumber);

    List<CertificateDetails> findByCertificateAuthority(boolean certificateAuthority);

    default List<CertificateDetails> findIssuerCertificates() {
        return findByCertificateAuthority(true);
    }
}
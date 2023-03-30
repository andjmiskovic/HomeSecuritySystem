package com.team4.secureit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDetails {

    @Id
    @Column(nullable = false, unique = true)
    private BigInteger serialNumber;

    @Column(unique = true)
    private String keystoreAlias;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String issuer;

    @Column(nullable = false)
    private Date notBefore;

    @Column(nullable = false)
    private Date notAfter;

    @Column(nullable = false)
    private String signatureAlgorithm;

    @Column(nullable = false)
    private int version;

    @Column(nullable = false)
    private String publicKeyAlgorithm;

    @Column(nullable = false)
    private String publicKeyFormat;

    @Column(nullable = false)
    private boolean certificateAuthority;
}

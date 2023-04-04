package com.team4.secureit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @Column
    private BigInteger serialNumber;

    @Column(length = 4096)
    private String pem;

    @Column(unique = true)
    private String alias;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User subscriber;
}

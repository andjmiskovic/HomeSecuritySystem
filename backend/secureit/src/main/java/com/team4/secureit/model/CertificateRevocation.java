package com.team4.secureit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigInteger;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
public class CertificateRevocation {

    @Id
    @Column
    private BigInteger serialNumber;

    @Column
    private String revocationReason;

    @CreationTimestamp
    private Instant created;

    @UpdateTimestamp
    private Instant modified;

    public CertificateRevocation(BigInteger serialNumber, String revocationReason) {
        this.serialNumber = serialNumber;
        this.revocationReason = revocationReason;
    }
}

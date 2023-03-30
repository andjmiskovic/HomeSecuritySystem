package com.team4.secureit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "PERSISTED_CSR")
@NoArgsConstructor
public class PersistedCSR {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @Column(length = 4096)
    private String pem;

    @Column
    private String alias;

    @Column(nullable = false)
    private String commonName;

    @Column(nullable = false)
    private String organization;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String algorithm;

    @Column(nullable = false)
    private int keySize;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    @Column
    private String rejectionReason;

    @Column
    private Instant processed;

    @CreationTimestamp
    private Instant created;

    @UpdateTimestamp
    private Instant modified;

}

package com.team4.secureit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Table(name = "CSR_DETAILS")
@NoArgsConstructor
public class CSRDetails {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @Column(length = 4096)
    private String csrPem;

    @Column(length = 4096)
    @JsonIgnore
    private String privateKeyPem;

    @Column(length = 4096)
    private String publicKeyPem;

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rejectionReason;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Instant processed;

    @CreationTimestamp
    private Instant created;

    @UpdateTimestamp
    private Instant modified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User subscriber;
}

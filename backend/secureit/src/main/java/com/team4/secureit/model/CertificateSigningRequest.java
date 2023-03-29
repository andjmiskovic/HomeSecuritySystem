package com.team4.secureit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class CertificateSigningRequest {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;
    private String algorithm;
    private String alias;
    @Column(unique = true, nullable = false)
    private String email;
    private int keySize;
    private int version;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Extension> extensions;
    private RequestStatus status;
    private LocalDateTime created;
    private LocalDateTime processed;
    private LocalDateTime validUntil;
    private String rejectionReason;
}

package com.team4.secureit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class DevicePairingRequest {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @Column(unique = true, nullable = false)
    private String code;

    @ManyToOne
    private PropertyOwner requestedBy;

    @ManyToOne
    private Property property;

    @CreationTimestamp
    private Instant created;

    @UpdateTimestamp
    private Instant modified;

    public DevicePairingRequest(String code, PropertyOwner requestedBy, Property property) {
        this.code = code;
        this.requestedBy = requestedBy;
        this.property = property;
    }
}

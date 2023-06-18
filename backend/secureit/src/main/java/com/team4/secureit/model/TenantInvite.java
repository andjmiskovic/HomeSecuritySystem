package com.team4.secureit.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TenantInvite {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @JoinColumn(nullable = false)
    @ManyToOne
    private Property property;

    @JoinColumn(nullable = false)
    @ManyToOne
    private PropertyOwner user;

    @Column(unique = true)
    private String verificationCode;

    @Column(nullable = false)
    private boolean verified = Boolean.FALSE;
}

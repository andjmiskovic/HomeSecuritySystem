package com.team4.secureit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Property {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    private String name;
    private String address;
    private PropertyType type;
    private String image;
    private UUID ownerId;
    @ManyToMany
    private Set<UUID> tenantsId;
}

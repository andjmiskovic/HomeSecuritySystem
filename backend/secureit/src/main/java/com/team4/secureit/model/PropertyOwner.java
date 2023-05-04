package com.team4.secureit.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class PropertyOwner extends User {

    @ManyToMany
    private Set<Property> ownedProperties;

    @ManyToMany
    private Set<Property> tenantProperties;
}

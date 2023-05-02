package com.team4.secureit.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
public class PropertyOwner extends User {

    @OneToMany
    private Set<Property> ownedProperties;

    @OneToMany
    private Set<Property> tenantProperties;
}

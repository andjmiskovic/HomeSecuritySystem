package com.team4.secureit.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@SQLDelete(sql = "UPDATE PROPERTY SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@NoArgsConstructor
public class Property {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    private String name;
    private String address;
    private PropertyType type;
    private String image;

    @ManyToMany()
    private List<PropertyOwner> tenants;

    @ManyToOne(cascade = CascadeType.ALL)
    private PropertyOwner owner;

    @Column(nullable = false)
    private boolean deleted = Boolean.FALSE;
}

package com.team4.secureit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @Column(columnDefinition = "uuid")
    private UUID id = UUID.randomUUID();

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private String manufacturer;

    @Column(length = 17)
    private String macAddress;

    @Column
    private String label;

    @Column(length = 4096)
    private String publicKeyPem;

    @ManyToOne
    private Property property;

    @ManyToOne
    private PropertyOwner user;

    public Device(String name, String type, String manufacturer, String macAddress, String label, String publicKeyPem, Property property, PropertyOwner user) {
        this.name = name;
        this.type = type;
        this.manufacturer = manufacturer;
        this.macAddress = macAddress;
        this.label = label;
        this.publicKeyPem = publicKeyPem;
        this.property = property;
        this.user = user;
    }
}

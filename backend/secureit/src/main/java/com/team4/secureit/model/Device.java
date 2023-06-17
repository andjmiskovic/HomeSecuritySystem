package com.team4.secureit.model;

import com.team4.secureit.dto.request.DeviceSensorInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Column
    private String sensorNames;

    @Column
    private String units;

    @ManyToOne
    private Property property;

    @ManyToOne
    private PropertyOwner user;

    public Device(String name, String type, String manufacturer, String macAddress, String label, String publicKeyPem, String sensorNames, String units, Property property, PropertyOwner user) {
        this.name = name;
        this.type = type;
        this.manufacturer = manufacturer;
        this.macAddress = macAddress;
        this.label = label;
        this.publicKeyPem = publicKeyPem;
        this.sensorNames = sensorNames;
        this.units = units;
        this.property = property;
        this.user = user;
    }

    public List<DeviceSensorInfo> getSensorInfo() {
        List<String> sensorNamesList = Arrays.asList(sensorNames.split(","));
        List<String> unitsList = Arrays.asList(units.split(","));

        return sensorNamesList.stream()
                .map(name -> new DeviceSensorInfo(name, unitsList.get(sensorNamesList.indexOf(name))))
                .collect(Collectors.toList());
    }
}

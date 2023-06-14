package com.team4.secureit.repository;

import com.team4.secureit.model.Device;
import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {

    List<Device> findByUser(PropertyOwner propertyOwner);

    List<Device> findByProperty(Property property);
}

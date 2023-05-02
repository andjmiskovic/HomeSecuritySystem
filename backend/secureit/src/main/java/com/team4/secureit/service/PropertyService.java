package com.team4.secureit.service;

import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyType;
import com.team4.secureit.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;
    public List<Property> getProperties(String search, PropertyType type) {
        return propertyRepository.getAll(search, type);
    }
}

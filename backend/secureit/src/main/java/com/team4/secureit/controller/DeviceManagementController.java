package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.DeviceInfo;
import com.team4.secureit.dto.request.DevicePairingInitRequest;
import com.team4.secureit.dto.response.CodeResponse;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.service.DeviceManagementService;
import com.team4.secureit.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/devices")
public class DeviceManagementController {

    @Autowired
    private DeviceManagementService deviceManagementService;

    @Autowired
    private PropertyService propertyService;

    @PostMapping("/init")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public CodeResponse initDevicePairing(@RequestBody @Valid DevicePairingInitRequest pairingRequest, Authentication authentication) {
        PropertyOwner propertyOwner = (PropertyOwner) authentication.getPrincipal();
        return deviceManagementService.initDevicePairing(pairingRequest.getPropertyId(), propertyOwner);
    }

    @PostMapping("/connect/{code}")
    public ResponseOk attemptDevicePairing(@RequestBody @Valid DeviceInfo deviceInfo, @PathVariable String code) {
        deviceManagementService.attemptDevicePairing(deviceInfo, code);
        return new ResponseOk("Pairing request sent.");
    }

    @PostMapping("/confirm/{code}")
    public ResponseOk finishDevicePairing(@PathVariable String code, Authentication authentication) {
        PropertyOwner propertyOwner = (PropertyOwner) authentication.getPrincipal();
        deviceManagementService.finishDevicePairing(code, propertyOwner);
        return new ResponseOk("Device paired successfully.");
    }
}

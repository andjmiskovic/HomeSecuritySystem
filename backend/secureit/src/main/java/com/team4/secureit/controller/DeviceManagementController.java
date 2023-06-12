package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.DeviceInfo;
import com.team4.secureit.dto.request.DevicePairingInitRequest;
import com.team4.secureit.dto.response.CodeResponse;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.service.DeviceManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(path = "/devices")
public class DeviceManagementController {

    @Autowired
    private DeviceManagementService deviceManagementService;

    @PostMapping("/init")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public CodeResponse initializePairing(@RequestBody @Valid DevicePairingInitRequest pairingRequest, Authentication authentication) {
        PropertyOwner propertyOwner = (PropertyOwner) authentication.getPrincipal();
        return deviceManagementService.initializePairing(pairingRequest.getPropertyId(), propertyOwner);
    }

    @PostMapping("/handshake/device/{code}")
    public DeferredResult<ResponseEntity<?>> handshakeDevice(@RequestBody @Valid DeviceInfo deviceInfo, @PathVariable String code) {
        return deviceManagementService.handshakeDevice(deviceInfo, code);
    }

    @PostMapping("/handshake/web/{code}")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public ResponseOk handshakeWeb(@PathVariable String code, Authentication authentication) {
        PropertyOwner propertyOwner = (PropertyOwner) authentication.getPrincipal();
        deviceManagementService.handshakeWeb(code, propertyOwner);
        return new ResponseOk("Device paired successfully.");
    }
}

package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.DeviceChangeAlarmsRequest;
import com.team4.secureit.dto.request.DeviceHandshakeData;
import com.team4.secureit.dto.request.DevicePairingInitRequest;
import com.team4.secureit.dto.request.ReportRequest;
import com.team4.secureit.dto.response.AlarmItem;
import com.team4.secureit.dto.response.CodeResponse;
import com.team4.secureit.dto.response.DeviceDetailsResponse;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.service.DeviceManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.List;
import java.util.UUID;

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
    public DeferredResult<ResponseEntity<?>> handshakeDevice(@RequestBody @Valid DeviceHandshakeData deviceHandshakeData, @PathVariable String code) {
        return deviceManagementService.handshakeDevice(deviceHandshakeData, code);
    }

    @PostMapping("/handshake/web/{code}")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public ResponseOk handshakeWeb(@PathVariable String code, Authentication authentication) {
        PropertyOwner propertyOwner = (PropertyOwner) authentication.getPrincipal();
        deviceManagementService.handshakeWeb(code, propertyOwner);
        return new ResponseOk("Device paired successfully.");
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public List<DeviceDetailsResponse> getDevices(Authentication authentication) {
        PropertyOwner propertyOwner = (PropertyOwner) authentication.getPrincipal();
        return deviceManagementService.getUsersDevices(propertyOwner);
    }

    @GetMapping("/{deviceId}")
    @PreAuthorize("hasRole('PROPERTY_OWNER')")
    public DeviceDetailsResponse getDevices(@PathVariable UUID deviceId) {
        return deviceManagementService.getDevice(deviceId);
    }

    @PostMapping("/{deviceId}")
    public ResponseOk changeAlarms(@RequestBody @Valid DeviceChangeAlarmsRequest alarmsRequest, @PathVariable UUID deviceId, Authentication authentication) {
        PropertyOwner propertyOwner = (PropertyOwner) authentication.getPrincipal();
        deviceManagementService.changeAlarms(deviceId, propertyOwner, alarmsRequest);
        return new ResponseOk("Alarms updated.");
    }

    @PostMapping("/{deviceId}/alarms")
    public AlarmItem getAlarms(@PathVariable UUID deviceId, Authentication authentication) {
        return deviceManagementService.getAlarms(deviceId);
    }

    @GetMapping("/report")
    public ByteArrayInputStream getReport(@RequestBody ReportRequest reportRequest) throws IOException {
        return deviceManagementService.generateReport(reportRequest);
    }
}

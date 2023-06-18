package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.DeviceChangeAlarmsRequest;
import com.team4.secureit.dto.request.DeviceHandshakeData;
import com.team4.secureit.dto.request.DevicePairingInitRequest;
import com.team4.secureit.dto.response.AlarmItem;
import com.team4.secureit.dto.response.CodeResponse;
import com.team4.secureit.dto.response.DeviceDetailsResponse;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.model.User;
import com.team4.secureit.service.DeviceManagementService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
    public List<AlarmItem> getAlarms(@PathVariable UUID deviceId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return deviceManagementService.getAlarms(deviceId, user);
    }

    @GetMapping(value = "/report", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getReport(@RequestParam String start, @RequestParam String end, @RequestParam String deviceId) throws IOException {
        ByteArrayInputStream byteFile = deviceManagementService.generateReport(start, end, deviceId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteFile));
    }
}

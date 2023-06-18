package com.team4.secureit.service;

import com.team4.secureit.api.ResponseError;
import com.team4.secureit.dto.request.DeviceHandshakeData;
import com.team4.secureit.dto.request.DeviceSensorInfo;
import com.team4.secureit.dto.response.CodeResponse;
import com.team4.secureit.dto.response.DeviceDetailsResponse;
import com.team4.secureit.dto.response.DeviceSuccessfulPairingResponse;
import com.team4.secureit.exception.DeviceNotFoundException;
import com.team4.secureit.exception.PairingRequestNotFound;
import com.team4.secureit.exception.PropertyNotFoundException;
import com.team4.secureit.model.*;
import com.team4.secureit.repository.DeviceRepository;
import com.team4.secureit.repository.PropertyRepository;
import com.team4.secureit.util.DroolsUtils;
import com.team4.secureit.util.MappingUtils;
import org.drools.template.DataProvider;
import org.drools.template.objects.ArrayDataProvider;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static com.team4.secureit.util.DroolsUtils.AS_FLOAT;
import static com.team4.secureit.util.MappingUtils.toDeviceDetailsResponse;

@Service
public class DeviceManagementService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private LogService logService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private final SecureRandom random = new SecureRandom();

    private final ConcurrentMap<String, DevicePairingRequest> attemptedPairings = new ConcurrentHashMap<>();

    public CodeResponse initializePairing(UUID propertyId, PropertyOwner propertyOwner) {
        Property property = propertyRepository
                .getPropertyByIdAndOwner(propertyId, propertyOwner)
                .orElseThrow(PropertyNotFoundException::new);

        String code;
        do {
            code = String.format("%06d", random.nextInt(1000000));
        } while (attemptedPairings.containsKey(code) && !attemptedPairings.get(code).isExpired());

        DevicePairingRequest request = new DevicePairingRequest(propertyOwner, property);
        attemptedPairings.put(code, request);

        logService.log(
                "User " + propertyOwner.getEmail() + " has initialized pairing.",
                LogSource.DEVICE_MANAGEMENT,
                null,
                propertyOwner.getId(),
                LogType.INFO
        );

        return new CodeResponse(code);
    }

    public DeferredResult<ResponseEntity<?>> handshakeDevice(DeviceHandshakeData deviceHandshakeData, String code) {
        /*
        This method is invoked by the device, and it begins the handshake process for the initiated pairing.
         */
        DevicePairingRequest pairing = attemptedPairings.get(code);
        if (pairing == null || pairing.isExpired()) {
            DeferredResult<ResponseEntity<?>> response = new DeferredResult<>();
            ResponseEntity<?> error = ResponseEntity
                    .status(HttpStatus.NOT_FOUND.value())
                    .body(new ResponseError(HttpStatus.NOT_FOUND, "Pin is invalid or it has expired. Try again."));

            response.setResult(error);
            return response;
        }

        pairing.setDeviceHandshakeData(deviceHandshakeData);
        pairing.setExpiresAt(Instant.now().plus(1, ChronoUnit.MINUTES));

        // Notify user about device pairing
        PropertyOwner propertyOwner = pairing.getRequestedBy();
        messagingTemplate.convertAndSendToUser(propertyOwner.getUsername(), "/queue/devices", deviceHandshakeData);

        logService.log(
                "Pairing request (pin " + code + ") accepted on the device.",
                LogSource.DEVICE_MANAGEMENT,
                null,
                propertyOwner.getId(),
                LogType.INFO
        );

        return pairing.getResponse(); // Response is returned after handshakeWeb() method is called, or when it times out.
    }

    public void handshakeWeb(String code, PropertyOwner propertyOwner) {
        /*
        This method is invoked by the user, and it completes the handshake process.
         */
        DevicePairingRequest pairing = attemptedPairings.get(code);
        if (pairing == null || pairing.isExpired() || !pairing.getRequestedBy().equals(propertyOwner))
            throw new PairingRequestNotFound();

        DeviceHandshakeData handshakeData = pairing.getDeviceHandshakeData();
        Device pairedDevice = new Device(
                handshakeData.getName(),
                handshakeData.getType(),
                handshakeData.getManufacturer(),
                handshakeData.getMacAddress(),
                handshakeData.getLabel(),
                handshakeData.getPublicKey(),
                handshakeData.getSensors().stream().map(DeviceSensorInfo::getName).collect(Collectors.joining(",")),
                handshakeData.getSensors().stream().map(DeviceSensorInfo::getUnit).collect(Collectors.joining(",")),
                pairing.getProperty(),
                pairing.getRequestedBy()
        );

        DataProvider dataProvider = new ArrayDataProvider(new String[][]{
                new String[]{"temperature", ">=", "49", AS_FLOAT},
                new String[]{"humidity", ">=", "79", AS_FLOAT},
        });

        String drl = DroolsUtils.renderDRL("basic.drt", dataProvider);
        System.out.println(drl);

        KieSession kieSession = DroolsUtils.createKieSessionFromDRL(drl);
        DroolsUtils.setKieSession(pairedDevice, kieSession);

        deviceRepository.save(pairedDevice);
        attemptedPairings.remove(code);

        pairing.getResponse().setResult(
                ResponseEntity.ok().body(new DeviceSuccessfulPairingResponse(pairedDevice.getId()))
        );

        logService.log(
                "Pairing request (pin " + code + ") accepted on the webapp. Device '" + pairedDevice.getLabel() + "' is now connected.",
                LogSource.DEVICE_MANAGEMENT,
                pairedDevice.getId(),
                propertyOwner.getId(),
                LogType.INFO
        );
    }

    @Scheduled(cron = "* 2 * * * *")
    private void removeExpiredPairings() {
        Instant now = Instant.now();
        attemptedPairings.forEach((key, value) -> {
            if (value.isExpired(now))
                attemptedPairings.remove(key, value);
        });

        logService.log(
                "Cleared expired pairings from memory.",
                LogSource.DEVICE_MANAGEMENT,
                LogType.INFO
        );
    }

    public List<DeviceDetailsResponse> getUsersDevices(PropertyOwner propertyOwner) {
        return deviceRepository.findByUser(propertyOwner).stream()
                .map(MappingUtils::toDeviceDetailsResponse)
                .toList();
    }

    public DeviceDetailsResponse getDevice(String id) {
        return toDeviceDetailsResponse(deviceRepository.findById(UUID.fromString(id)).orElseThrow(DeviceNotFoundException::new));
    }
}

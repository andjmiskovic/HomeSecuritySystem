package com.team4.secureit.service;

import com.team4.secureit.api.ResponseError;
import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.DeviceInfo;
import com.team4.secureit.dto.response.CodeResponse;
import com.team4.secureit.exception.PairingRequestNotFound;
import com.team4.secureit.exception.PropertyNotFoundException;
import com.team4.secureit.model.DevicePairingRequest;
import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.repository.PropertyRepository;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class DeviceManagementService {

    @Autowired
    private PropertyRepository propertyRepository;

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

        return new CodeResponse(code);
    }

    public DeferredResult<ResponseEntity<?>> handshakeDevice(DeviceInfo deviceInfo, String code) {
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

        pairing.setDeviceHandshake(true);
        pairing.setDeviceInfo(deviceInfo);
        pairing.setExpiresAt(Instant.now().plus(1, ChronoUnit.MINUTES));

        // Notify user about device pairing
        PropertyOwner propertyOwner = pairing.getRequestedBy();
        messagingTemplate.convertAndSendToUser(propertyOwner.getUsername(), "/queue/devices", deviceInfo);

        System.out.println("Is this your device?: " + deviceInfo.getMacAddress() + " " + deviceInfo.getLabel());

        return pairing.getResponse(); // Response is returned after handshakeWeb() method is called, or if it times out
    }

    public void handshakeWeb(String code, PropertyOwner propertyOwner) {
        /*
        This method is invoked by the user, and it completes the handshake process.
         */
        DevicePairingRequest pairing = attemptedPairings.get(code);
        if (pairing == null || pairing.isExpired() || !pairing.getRequestedBy().equals(propertyOwner))
            throw new PairingRequestNotFound();

        pairing.setUserHandshake(true);

        DeferredResult<ResponseEntity<?>> response = pairing.getResponse();
        ResponseEntity<?> success = ResponseEntity
                .ok()
                .body(new ResponseOk("TODO: Send data useful for setting up the device."));
        response.setResult(success);

        attemptedPairings.remove(code);
    }

    @Scheduled(cron = "* 2 * * * *")
    private void removeExpiredPairings() {
        Instant now = Instant.now();
        attemptedPairings.forEach((key, value) -> {
            if (value.isExpired(now))
                attemptedPairings.remove(key, value);
        });
    }
}

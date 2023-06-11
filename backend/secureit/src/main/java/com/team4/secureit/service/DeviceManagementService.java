package com.team4.secureit.service;

import com.team4.secureit.dto.request.DeviceInfo;
import com.team4.secureit.dto.response.CodeResponse;
import com.team4.secureit.exception.PropertyNotFoundException;
import com.team4.secureit.exception.RequestNotFoundException;
import com.team4.secureit.model.DevicePairingRequest;
import com.team4.secureit.model.Property;
import com.team4.secureit.model.PropertyOwner;
import com.team4.secureit.repository.DevicePairingRequestRepository;
import com.team4.secureit.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceManagementService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private DevicePairingRequestRepository devicePairingRequestRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final SecureRandom random = new SecureRandom();

    public CodeResponse initDevicePairing(UUID propertyId, PropertyOwner propertyOwner) {
        Property property = propertyRepository
                .getPropertyByIdAndOwner(propertyId, propertyOwner)
                .orElseThrow(PropertyNotFoundException::new);

        Instant fiveMinutesAgo = Instant.now().minus(5, ChronoUnit.MINUTES);
        String code;
        do {
            code = String.format("%06d", random.nextInt(1000000));
        } while (!devicePairingRequestRepository.existsByCodeAndCreatedAfter(code, fiveMinutesAgo));

        DevicePairingRequest request = new DevicePairingRequest(code, propertyOwner, property);
        devicePairingRequestRepository.save(request);

        return new CodeResponse(code);
    }

    public void attemptDevicePairing(DeviceInfo deviceInfo, String code) {
        Optional<DevicePairingRequest> request = devicePairingRequestRepository.getByCode(code);
        if (request.isEmpty())
            return;

        PropertyOwner propertyOwner = request.get().getRequestedBy();
        messagingTemplate.convertAndSendToUser(propertyOwner.getUsername(), "/queue/devices", deviceInfo);
    }

    public void finishDevicePairing(String code, PropertyOwner propertyOwner) {
        DevicePairingRequest request = devicePairingRequestRepository
                .getByCode(code)
                .orElseThrow(RequestNotFoundException::new);

        if (!request.getRequestedBy().equals(propertyOwner))
            throw new RequestNotFoundException();

        // TODO: Finish
    }
}

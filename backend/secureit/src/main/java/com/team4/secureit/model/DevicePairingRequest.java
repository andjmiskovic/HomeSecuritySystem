package com.team4.secureit.model;

import com.team4.secureit.api.ResponseError;
import com.team4.secureit.dto.request.DeviceHandshakeData;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
public class DevicePairingRequest {

    private PropertyOwner requestedBy;

    private Property property;

    private Instant expiresAt = Instant.now().plus(5, ChronoUnit.MINUTES);

    private DeviceHandshakeData deviceHandshakeData = null;

    private DeferredResult<ResponseEntity<?>> response = new DeferredResult<>(60 * 1000L);

    public DevicePairingRequest(PropertyOwner requestedBy, Property property) {
        this.requestedBy = requestedBy;
        this.property = property;

        this.response.onTimeout(() ->
            response.setErrorResult(
                    ResponseEntity
                            .status(HttpStatus.REQUEST_TIMEOUT.value())
                            .body(new ResponseError(HttpStatus.REQUEST_TIMEOUT, "No response from web app. Handshake timed out."))
            )
        );
    }

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    public boolean isExpired(Instant at) {
        return expiresAt.isBefore(at);
    }

}

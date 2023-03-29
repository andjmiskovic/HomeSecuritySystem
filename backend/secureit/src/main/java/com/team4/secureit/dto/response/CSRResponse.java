package com.team4.secureit.dto.response;

import com.team4.secureit.model.CertificateSigningRequest;
import com.team4.secureit.model.Extension;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Data
@AllArgsConstructor
public class CSRResponse {
    private UUID id;
    private String algorithm;
    private String alias;
    private String email;
    private int keySize;
    private int version;
    private List<String> extensions;
    private String status;
    private LocalDateTime created;
    private LocalDateTime processed;
    private LocalDateTime validUntil;

    public CSRResponse(CertificateSigningRequest request) {
        this.id = request.getId();
        this.algorithm = request.getAlgorithm();
        this.alias = request.getAlias();
        this.email = request.getEmail();
        this.extensions = request.getExtensions().stream().map(Extension::getName).collect(Collectors.toList());
        this.keySize = request.getKeySize();
        this.version = request.getVersion();
        this.created = request.getCreated();
        this.processed = request.getProcessed();
        this.validUntil = request.getValidUntil();
        this.status = request.getStatus().toString();
    }
}

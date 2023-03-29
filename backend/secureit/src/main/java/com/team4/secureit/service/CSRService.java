package com.team4.secureit.service;

import com.team4.secureit.dto.response.CSRResponse;
import com.team4.secureit.model.CertificateSigningRequest;
import com.team4.secureit.model.IssuerData;
import com.team4.secureit.model.RequestStatus;
import com.team4.secureit.model.SubjectData;
import com.team4.secureit.repository.CSRRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CSRService {

    private final CSRRepository csrRepository;
    private final CertificateService certificateService;

    private List<CSRResponse> csrMapper(List<CertificateSigningRequest> requests) {
        List<CSRResponse> responses = new ArrayList<>();
        requests.forEach(request -> responses.add(new CSRResponse(request)));
        return responses;
    }

    public void createCSR(@Valid CertificateSigningRequest csrRequest) {
        csrRequest.setStatus(RequestStatus.PENDING);
        csrRequest.setCreated(LocalDateTime.now());
        csrRepository.save(csrRequest);
    }

    public List<CSRResponse> getAll() {
        return csrMapper(csrRepository.findAll());
    }

    public List<CSRResponse> getByStatus(String status) {
        return csrMapper(csrRepository.findByStatus(RequestStatus.valueOf(status)));
    }

    public CSRResponse getById(UUID id) throws EntityNotFoundException {
        return new CSRResponse(get(id));
    }

    public CertificateSigningRequest get(UUID id) throws EntityNotFoundException {
        return csrRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public CSRResponse approve(UUID id, String adminEmail) {
        CertificateSigningRequest request = get(id);

        request.setStatus(RequestStatus.ACCEPTED);
        request.setProcessed(LocalDateTime.now());
        // TODO: create and save certificate
//        X509Certificate certificate = certificateService.generateCertificate(new SubjectData(), new IssuerData(), request);

        csrRepository.save(request);
        return new CSRResponse(request);
    }

    public CSRResponse reject(UUID id, String reason) {
        CertificateSigningRequest request = get(id);

        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(reason);
        request.setProcessed(LocalDateTime.now());

        csrRepository.save(request);
        return new CSRResponse(request);
    }
}

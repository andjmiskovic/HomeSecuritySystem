package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.response.CSRResponse;
import com.team4.secureit.model.CertificateSigningRequest;
import com.team4.secureit.service.CSRService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/csr")
public class CSRController {

    private final CSRService csrService;

    @PostMapping()
    public ResponseOk create(@RequestBody @Valid final CertificateSigningRequest csrRequest) {
        this.csrService.createCSR(csrRequest);
        return new ResponseOk("Successfully created CSR.");
    }

    @GetMapping()
    public List<CSRResponse> getAll() {
        return this.csrService.getAll();
    }

    @GetMapping("/all/{status}")
    public List<CSRResponse> getAll(@PathVariable String status) {
        return this.csrService.getByStatus(status);
    }

    @GetMapping("{id}")
    public CSRResponse get(@PathVariable final UUID id) throws EntityNotFoundException {
        return this.csrService.getById(id);
    }

    @PutMapping("approve/{id}")
    public CSRResponse approve(@PathVariable final UUID id, @RequestBody final String adminEmail) throws EntityNotFoundException {
        return this.csrService.approve(id, adminEmail);
    }

    @PutMapping("reject/{id}")
    public CSRResponse reject(@PathVariable final UUID id, @RequestBody final String rejectionReason) throws EntityNotFoundException {
        return this.csrService.reject(id, rejectionReason);
    }
}

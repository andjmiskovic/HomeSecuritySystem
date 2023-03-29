package com.team4.secureit.validation;

import com.team4.secureit.dto.request.CSRCreationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class AlgorithmAndKeySizeValidator implements ConstraintValidator<AlgorithmAndKeySize, CSRCreationRequest> {

    private Set<String> allowedAlgorithms;

    @Override
    public void initialize(AlgorithmAndKeySize constraintAnnotation) {
        allowedAlgorithms = Set.of("RSA", "DSA", "EC");
    }

    @Override
    public boolean isValid(CSRCreationRequest request, ConstraintValidatorContext constraintValidatorContext) {
        String algorithm = request.getAlgorithm().toUpperCase();

        if (!allowedAlgorithms.contains(algorithm))
            return false;

        int keySize = request.getKeySize();
        return switch (algorithm) {
            case "RSA" -> keySize >= 512 && keySize <= 16384;
            case "DSA" -> keySize >= 512 && keySize <= 1024;
            case "EC" -> keySize >= 192 && keySize <= 521;
            default -> false;
        };
    }
}

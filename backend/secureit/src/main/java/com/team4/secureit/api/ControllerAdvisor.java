package com.team4.secureit.api;

import com.team4.secureit.exception.*;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvisor {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseError handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        e.getBindingResult().getGlobalErrors().forEach(error ->
                errors.put(error.getObjectName(), error.getDefaultMessage())
        );

        return new ResponseError(HttpStatus.BAD_REQUEST, "Field validation failed.", errors);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseError handleEmailAlreadyInUseException(EmailAlreadyInUseException e) {
        return new ResponseError(HttpStatus.CONFLICT, "Email address is used by another user.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(EmailAlreadyVerifiedException.class)
    public ResponseError handleEmailAlreadyVerifiedException(EmailAlreadyVerifiedException e) {
        return new ResponseError(HttpStatus.CONFLICT, "Email address is already verified.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseError handlePasswordsDoNotMatchException(PasswordsDoNotMatchException e) {
        return new ResponseError(HttpStatus.CONFLICT, "Passwords do not match.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseError handleInvalidVerificationCodeException(InvalidVerificationCodeException e) {
        return new ResponseError(HttpStatus.BAD_REQUEST, "Invalid verification code.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseError handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseError(HttpStatus.NOT_FOUND, "User not found.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseError handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseError(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CertificateAlreadyRevokedException.class)
    public ResponseError handleCertificateAlreadyRevokedException(CertificateAlreadyRevokedException e) {
        return new ResponseError(HttpStatus.CONFLICT, "Certificate has already been revoked.");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(VerificationFailedException.class)
    public ResponseError handleVerificationFailedException(VerificationFailedException e) {
        return new ResponseError(HttpStatus.FORBIDDEN, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ResponseError handleJwtExceptions(JwtException e) {
        return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LockedException.class)
    public ResponseError handleLockedException(LockedException e) {
        return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Missing2FaCodeException.class)
    public ResponseError handleMissing2FaCodeException(Missing2FaCodeException e) {
        return new ResponseError(HttpStatus.OK, e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(Invalid2FaCodeException.class)
    public ResponseError handleInvalid2FaCodeException(Invalid2FaCodeException e) {
        return new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PairingRequestNotFound.class)
    public ResponseError handlePairingRequestNotFound(PairingRequestNotFound e) {
        return new ResponseError(HttpStatus.NOT_FOUND, "Pairing request not found.");
    }
}

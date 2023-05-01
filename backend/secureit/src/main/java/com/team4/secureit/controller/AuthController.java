package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.*;
import com.team4.secureit.dto.response.LoginResponse;
import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.model.User;
import com.team4.secureit.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return accountService.login(loginRequest, response);
    }

    @PostMapping("/logout")
    public ResponseOk logout(HttpServletRequest request, HttpServletResponse response) {
        accountService.logout(request, response);
        return new ResponseOk("Success");
    }

    @PostMapping("/register")
    public ResponseOk register(@Valid @RequestBody RegistrationRequest registrationRequest) {
        accountService.registerPropertyOwner(registrationRequest);
        return new ResponseOk("User registered successfully.");
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk create(@Valid @RequestBody UserDetailsRequest createUserRequest) {
        accountService.createPropertyOwner(createUserRequest);
        return new ResponseOk("User created successfully.");
    }

    @PostMapping("/register/verify")
    public ResponseOk verify(@Valid @RequestBody VerificationRequest verificationRequest) {
        accountService.verifyEmail(verificationRequest);
        return new ResponseOk("Email verified successfully.");
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserInfoResponse getLoggedUserInfo(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return new UserInfoResponse(user);
    }

    @GetMapping("/is-password-set/{verificationCode}")
    public boolean isPasswordSet(@PathVariable String verificationCode) {
        return accountService.isPasswordSet(verificationCode);
    }

    @PutMapping("/set-password")
    public ResponseOk setPassword(@RequestBody SetPasswordRequest setPasswordRequest) {
        accountService.setPassword(setPasswordRequest);
        return new ResponseOk("Password set successfully. User is verified.");
    }
}

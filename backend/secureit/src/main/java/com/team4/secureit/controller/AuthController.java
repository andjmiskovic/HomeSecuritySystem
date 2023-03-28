package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.LoginRequest;
import com.team4.secureit.dto.request.RegistrationRequest;
import com.team4.secureit.dto.response.TokenResponse;
import com.team4.secureit.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
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
}
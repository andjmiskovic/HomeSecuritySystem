package com.team4.secureit.controller;

import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.model.User;
import com.team4.secureit.service.AccountService;
import com.team4.secureit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserInfoResponse> getPropertyOwners() {
        return userService.getPropertyOwners();
    }
}

package com.team4.secureit.controller;

import com.team4.secureit.api.ResponseOk;
import com.team4.secureit.dto.request.UserDetailsRequest;
import com.team4.secureit.dto.response.UserDetailResponse;
import com.team4.secureit.dto.response.UserInfoResponse;
import com.team4.secureit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{userEmail}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailResponse getPropertyOwner(@PathVariable String userEmail) {
        return userService.getPropertyOwner(userEmail);
    }

    @PutMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailResponse editPropertyOwner(@RequestBody UserDetailsRequest user) {
        return userService.editPropertyOwner(user);
    }

    @DeleteMapping("/{userEmail}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseOk deleteUser(@PathVariable String userEmail) {
        userService.deleteUser(userEmail);
        return new ResponseOk("User logically deleted.");
    }
}

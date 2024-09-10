package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.ApiResponse;
import com.chatApp.chatApp.dto.Response.AccountResponse;
import com.chatApp.chatApp.dto.Response.AuthenticationResponse;
import com.chatApp.chatApp.dto.request.AuthenticationRequest;
import com.chatApp.chatApp.services.AuthenticationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequestMapping("/authentication")
public class AuthenticationController {

    final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("")
    public ApiResponse<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        if(!authenticationResponse.isAuthenticated()) {
            return ApiResponse.<AuthenticationResponse>builder()
                    .code(200)
                    .message("Login Failed")
                    .data(authenticationService.authenticate(authenticationRequest))
                    .build();
        }

        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("Login Success")
                .data(authenticationService.authenticate(authenticationRequest))
                .build();
    }
}

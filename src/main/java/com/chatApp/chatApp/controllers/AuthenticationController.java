package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.ApiResponse;
import com.chatApp.chatApp.dto.Response.AuthenticationResponse;
import com.chatApp.chatApp.dto.request.AuthenticationRequest;
import com.chatApp.chatApp.services.AuthenticationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequestMapping("/authen")
public class AuthenticationController {

    final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public ApiResponse<AuthenticationResponse> authentication(AuthenticationRequest authenticationRequest) {
        authenticationService.authenticate(authenticationRequest);

//        return ApiResponse<AuthenticationResponse>.builder().;
    }
}

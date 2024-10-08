package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.ApiResponse;
import com.chatApp.chatApp.dto.Response.AccountResponse;
import com.chatApp.chatApp.dto.Response.AuthenticationResponse;
import com.chatApp.chatApp.dto.Response.IntrospectResponse;
import com.chatApp.chatApp.dto.request.AuthenticationRequest;
import com.chatApp.chatApp.dto.request.IntrospectRequest;
import com.chatApp.chatApp.dto.request.LogoutRequest;
import com.chatApp.chatApp.exception.AppException;
import com.chatApp.chatApp.model.InvalidatedToken;
import com.chatApp.chatApp.services.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequestMapping("/auth")
public class AuthenticationController {

    final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("")
    public ApiResponse<AuthenticationResponse> authentication(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);

        if (!authenticationResponse.isAuthenticated()) {
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

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenticationService.logout(request);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Logout success")
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        log.info("introspect");
        return ApiResponse.<IntrospectResponse>builder()
                .code(200)
                .data(authenticationService.introspect(request))
                .message("Introspect success")
                .build();
    }
}

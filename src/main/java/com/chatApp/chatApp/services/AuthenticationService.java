package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.Response.AuthenticationResponse;
import com.chatApp.chatApp.dto.request.AuthenticationRequest;
import com.chatApp.chatApp.exception.AppException;
import com.chatApp.chatApp.exception.ErrorCode;
import com.chatApp.chatApp.model.Account;
import com.chatApp.chatApp.model.Permission;
import com.chatApp.chatApp.model.Role;
import com.chatApp.chatApp.repositories.AccountRepository;
import com.chatApp.chatApp.repositories.PermissionRepository;
import com.chatApp.chatApp.repositories.RoleRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AuthenticationService {

    @NotNull
    @Value("${SIGNER_KEY}")
    protected String SIGNER_KEY;

    @NotNull
    @Value("${valid-duration}")
    protected long VALID_DURATION;

    @NotNull
    @Value("${refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    final AccountRepository accountRepository;
    final RoleRepository roleRepository;

    @Autowired
    public AuthenticationService(AccountRepository accountRepository, PermissionRepository permissionRepository,
                                 RoleRepository roleRepository) {

        this.accountRepository = accountRepository;
        this.roleRepository = roleRepository;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        var account = accountRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow( () -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(authenticationRequest.getPassword(), account.getPassword());

        if (!authenticated) {
            return AuthenticationResponse.builder()
                    .token("UNAUTHORIZED")
                    .code(401)
                    .authenticated(false)
                    .build();
        }

        String token = generateToken(account);

        return AuthenticationResponse.builder()
                .token(token)
                .code(200)
                .authenticated(true)
                .build();
    }

    private String generateToken(Account account) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getUsername())
                .issuer("chatAppNative.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(account))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));  // need an algorithm
            return jwsObject.serialize();
        } catch (JOSEException e) {
            System.out.println(("Cannot Create Token" + e));
            throw new RuntimeException(e);
        }

    }

    private String buildScope(Account account) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(account.getRoles())) {
            account.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role);

                Role roleModel = roleRepository.findByName(role);
                if (!CollectionUtils.isEmpty(roleModel.getPermissions())) {
                    roleModel.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }

}

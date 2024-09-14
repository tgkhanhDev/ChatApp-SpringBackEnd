package com.chatApp.chatApp.controllers;

import com.chatApp.chatApp.dto.ApiResponse;
import com.chatApp.chatApp.dto.Response.AccountResponse;
import com.chatApp.chatApp.dto.request.AccountRequest;
import com.chatApp.chatApp.model.Account;
import com.chatApp.chatApp.services.AccountService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
@RequestMapping("/account")
public class AccountController {

    final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_CR_ACCOUNT')")
    public ApiResponse<List<AccountResponse>> getAllAccount() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<AccountResponse>>builder()
                .code(200)
                .message("Get All Account Success")
                .data(accountService.getAllAccounts())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_CR_ACCOUNT')")
    public ApiResponse<AccountResponse> getAccountById(@PathVariable String id) {
        return ApiResponse.<AccountResponse>builder()
                .data(accountService.getAccountById(id))
                .message("Get Account Success")
                .code(200)
                .build();
    }

    @PostMapping("/createAccount")
    public ApiResponse<AccountResponse> saveAccount(@RequestBody AccountRequest accountRequest){
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .message("Create Account Success")
                .data(accountService.registerAccount(accountRequest))
                .build();
    }

    @PutMapping("/updateAccount/{id}")
    @PreAuthorize("hasAuthority('SCOPE_UD_ACCOUNT')")
    public ApiResponse<AccountResponse> updateAccount(@RequestBody AccountRequest accountRequest, @PathVariable String id){
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .message("Update Account Success")
                .data(accountService.updateAccount(accountRequest, id))
                .build();
    }

    @DeleteMapping("/deleteAccount/{id}")
    @PreAuthorize("hasAuthority('SCOPE_UD_ACCOUNT')")
    public ApiResponse<String> deleteAccountById(@PathVariable String id){
        accountService.deleteAccountById(id);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Delete Account Success")
                .build();
    }

    @GetMapping("/myInfo")
    @PreAuthorize("hasAuthority('SCOPE_VIEW_INFO')")
    public ApiResponse<AccountResponse> getMyInfo(){
        return ApiResponse.<AccountResponse>builder()
                .code(200)
                .message("Get My Info Success")
                .data(accountService.getMyInfo())
                .build();
    }

}

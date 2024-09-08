package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.Response.AccountResponse;
import com.chatApp.chatApp.dto.request.AccountRequest;
import com.chatApp.chatApp.model.Account;

import java.util.List;

public interface AccountService {

    List<AccountResponse> getAllAccounts();
    AccountResponse registerAccount(AccountRequest accountRequest);

    void deleteAccountById(String id);
    AccountResponse getAccountById(String id);
    AccountResponse updateAccount(AccountRequest accountRequest, String id);
}

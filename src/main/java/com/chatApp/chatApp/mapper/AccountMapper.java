package com.chatApp.chatApp.mapper;

import com.chatApp.chatApp.dto.Response.AccountResponse;
import com.chatApp.chatApp.dto.request.AccountRequest;
import com.chatApp.chatApp.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {
    Account toAccount(AccountRequest accountRequest);

    AccountResponse toAccountResponse(Account account);

    List<AccountResponse> toAccountResponseList(List<Account> accounts);
}

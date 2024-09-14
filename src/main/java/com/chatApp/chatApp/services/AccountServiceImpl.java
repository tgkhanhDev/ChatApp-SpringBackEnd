package com.chatApp.chatApp.services;

import com.chatApp.chatApp.dto.Response.AccountResponse;
import com.chatApp.chatApp.dto.request.AccountRequest;
import com.chatApp.chatApp.exception.AppException;
import com.chatApp.chatApp.exception.ErrorCode;
import com.chatApp.chatApp.mapper.AccountMapper;
import com.chatApp.chatApp.model.Account;
import com.chatApp.chatApp.repositories.AccountRepository;
import com.chatApp.chatApp.repositories.RoleRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountServiceImpl implements AccountService {

    final RoleRepository roleRepository;
    final AccountRepository accountRepository;
    final AccountMapper accountMapper;
    final RoleService roleService;
    final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, AccountMapper accountMapper,
                              RoleRepository roleRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream().map(accountMapper::toAccountResponse).toList();
    }

    @Override
    @Transactional
    public AccountResponse registerAccount(AccountRequest accountRequest) {
        if (accountRepository.existsByUsername(accountRequest.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        accountRequest.setPassword(passwordEncoder.encode(accountRequest.getPassword()));

        //Give it the default Role... which will auto save into db
        Set<String> roles = roleService.generateDefaultRole();
        accountRequest.setRoles(roles);

        return accountMapper.toAccountResponse(accountRepository.save(accountMapper.toAccount(accountRequest)));
    }

    @Override
    @Transactional
    public void deleteAccountById(String id) {
        if (!accountRepository.existsById(id)) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
        accountRepository.deleteById(id);
    }

    @Override
    public AccountResponse getAccountById(String id) {
        return accountRepository.findById(id)
                .map(accountMapper::toAccountResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
    }

    @Override
    @Transactional
    public AccountResponse updateAccount(AccountRequest accountRequest, String id) {

        if (!accountRepository.existsById(id)) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }

        if (accountRequest.getRoles().stream().noneMatch(role -> roleRepository.existsByName(role))) {
            throw new AppException(ErrorCode.ROLE_INVALID);
        }

        Account account = accountRepository.findById(id).get();

        account.setFirstName(accountRequest.getFirstName());
        account.setLastName(accountRequest.getLastName());
        account.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
        account.setDob(accountRequest.getDob());
        account.setRoles(accountRequest.getRoles());

        return accountMapper.toAccountResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account account = accountRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));
        System.out.println("My Info: "+ account.toString());
        return accountMapper.toAccountResponse(account);
    }

}

package com.bank.app.controller;

import com.bank.app.dto.CreateAccountRequest;
import com.bank.app.dto.CreateAccountResponse;
import com.bank.app.entity.Account;
import com.bank.app.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getAccount(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getAccount(accountId));
    }

    @PostMapping
    public ResponseEntity<CreateAccountResponse> createAccount(@RequestBody CreateAccountRequest request) {
        Account createdAccount = accountService.createAccount(request.getInitialBalance());
        return ResponseEntity.ok(new CreateAccountResponse(createdAccount.getId(), createdAccount.getAccountNumber()));
    }
}

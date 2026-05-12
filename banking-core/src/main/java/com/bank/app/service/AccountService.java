package com.bank.app.service;

import com.bank.app.entity.Account;
import com.bank.app.repository.AccountRepository;

import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));
    }

    public Account createAccount(Double initialBalance) {
        Account account = new Account();
        account.setBalance(initialBalance == null ? 0.0 : initialBalance);
        account = accountRepository.save(account);
        account.setAccountNumber("ACC-" + account.getId());
        return accountRepository.save(account);
    }
}

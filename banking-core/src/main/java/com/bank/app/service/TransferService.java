package com.bank.app.service;

import com.bank.app.dto.TransferRequest;
import com.bank.app.entity.Account;
import com.bank.app.entity.Transaction;
import com.bank.app.repository.AccountRepository;
import com.bank.app.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TransferService {

    private final EventService eventService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferService(EventService eventService,
                           AccountRepository accountRepository,
                           TransactionRepository transactionRepository) {
        this.eventService = eventService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void transfer(TransferRequest request) {
        if (request.getFromAccountId() == null || request.getToAccountId() == null || request.getAmount() == null) {
            throw new IllegalArgumentException("Transfer request must include fromAccountId, toAccountId, and amount");
        }
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Source account not found: " + request.getFromAccountId()));
        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Destination account not found: " + request.getToAccountId()));

        Double sourceBalance = fromAccount.getBalance() == null ? 0.0 : fromAccount.getBalance();
        Double destinationBalance = toAccount.getBalance() == null ? 0.0 : toAccount.getBalance();

        if (sourceBalance < request.getAmount()) {
            throw new IllegalArgumentException("Insufficient funds in source account");
        }

        fromAccount.setBalance(sourceBalance - request.getAmount());
        toAccount.setBalance(destinationBalance + request.getAmount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount.getAccountNumber());
        transaction.setToAccount(toAccount.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(Instant.now());
        transactionRepository.save(transaction);

        eventService.recordEvent("TRANSFER_COMPLETED", request.toString());
    }
}

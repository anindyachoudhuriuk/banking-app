package com.bank.app.service;

import org.springframework.stereotype.Service;

@Service
public class FraudActionService {

    public boolean isFlaggedTransaction(String accountNumber, double amount) {
        return amount > 10000;
    }
}

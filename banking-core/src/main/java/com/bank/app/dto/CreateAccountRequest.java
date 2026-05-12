package com.bank.app.dto;

public class CreateAccountRequest {
    private Double initialBalance;

    public Double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
    }
}

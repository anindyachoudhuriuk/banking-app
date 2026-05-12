package com.bank.app.dto;

public class CreateAccountResponse {
    private Long accountId;
    private String accountNumber;

    public CreateAccountResponse() {
    }

    public CreateAccountResponse(Long accountId, String accountNumber) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}

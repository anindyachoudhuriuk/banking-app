package com.bank.app.service;

import com.bank.app.dto.TransferRequest;
import com.bank.app.entity.Account;
import com.bank.app.entity.Device;
import com.bank.app.entity.Transaction;
import com.bank.app.repository.AccountRepository;
import com.bank.app.repository.DeviceRepository;
import com.bank.app.repository.TransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class TransferService {

    private final EventService eventService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final DeviceRepository deviceRepository;

    public TransferService(EventService eventService,
                           AccountRepository accountRepository,
                           TransactionRepository transactionRepository,
                           DeviceRepository deviceRepository) {
        this.eventService = eventService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.deviceRepository = deviceRepository;
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

        Device device = resolveDevice(request.getDeviceId(), request.getDeviceType(), request.getIpAddress());

        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromAccount.getAccountNumber());
        transaction.setToAccount(toAccount.getAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setTimestamp(Instant.now());
        transaction.setDeviceId(device.getDeviceId());
        transaction.setDeviceType(device.getDeviceType());
        transaction.setDeviceIpAddress(device.getIpAddress());
        transactionRepository.save(transaction);

        eventService.recordEvent("TRANSFER_COMPLETED", request.toString());
    }

    private Device resolveDevice(String deviceId, String deviceType, String ipAddress) {
        if (deviceId == null || deviceId.isBlank()) {
            throw new IllegalArgumentException("deviceId is required for transfer");
        }
        return deviceRepository.findByDeviceId(deviceId)
                .map(device -> {
                    boolean updated = false;
                    if (deviceType != null && !deviceType.equals(device.getDeviceType())) {
                        device.setDeviceType(deviceType);
                        updated = true;
                    }
                    if (ipAddress != null && !ipAddress.equals(device.getIpAddress())) {
                        device.setIpAddress(ipAddress);
                        updated = true;
                    }
                    return updated ? deviceRepository.save(device) : device;
                })
                .orElseGet(() -> {
                    Device device = new Device();
                    device.setDeviceId(deviceId);
                    device.setDeviceType(deviceType == null ? "UNKNOWN" : deviceType);
                    device.setIpAddress(ipAddress);
                    return deviceRepository.save(device);
                });
    }
}

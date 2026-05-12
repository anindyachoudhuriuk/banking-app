package com.bank.app;

import com.bank.app.entity.Account;
import com.bank.app.entity.Device;
import com.bank.app.entity.Transaction;
import com.bank.app.repository.AccountRepository;
import com.bank.app.repository.DeviceRepository;
import com.bank.app.repository.TransactionRepository;
import com.bank.app.service.AccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BankingCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankingCoreApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }

    @Bean
    public CommandLineRunner dataSeeder(DeviceRepository deviceRepository,
                                        AccountService accountService,
                                        AccountRepository accountRepository,
                                        TransactionRepository transactionRepository) {
        return args -> {
            if (deviceRepository.count() == 0) {
                Device mobile = new Device();
                mobile.setDeviceId("device-001");
                mobile.setDeviceType("MOBILE");
                mobile.setIpAddress("192.168.0.10");

                Device tablet = new Device();
                tablet.setDeviceId("device-002");
                tablet.setDeviceType("TABLET");
                tablet.setIpAddress("192.168.0.11");

                deviceRepository.save(mobile);
                deviceRepository.save(tablet);
            }

            if (accountRepository.count() == 0 && transactionRepository.count() == 0) {
                Account accountA = accountService.createAccount(1000.0);
                Account accountB = accountService.createAccount(500.0);

                Device mobile = deviceRepository.findByDeviceId("device-001").orElse(null);
                Device tablet = deviceRepository.findByDeviceId("device-002").orElse(null);

                Transaction t1 = new Transaction();
                t1.setFromAccount(accountA.getAccountNumber());
                t1.setToAccount(accountB.getAccountNumber());
                t1.setAmount(100.0);
                t1.setTimestamp(java.time.Instant.now());
                if (mobile != null) {
                    t1.setDeviceId(mobile.getDeviceId());
                    t1.setDeviceType(mobile.getDeviceType());
                    t1.setDeviceIpAddress(mobile.getIpAddress());
                }
                transactionRepository.save(t1);

                Transaction t2 = new Transaction();
                t2.setFromAccount(accountB.getAccountNumber());
                t2.setToAccount(accountA.getAccountNumber());
                t2.setAmount(50.0);
                t2.setTimestamp(java.time.Instant.now());
                if (tablet != null) {
                    t2.setDeviceId(tablet.getDeviceId());
                    t2.setDeviceType(tablet.getDeviceType());
                    t2.setDeviceIpAddress(tablet.getIpAddress());
                }
                transactionRepository.save(t2);

                Transaction t3 = new Transaction();
                t3.setFromAccount(accountA.getAccountNumber());
                t3.setToAccount(accountB.getAccountNumber());
                t3.setAmount(75.0);
                t3.setTimestamp(java.time.Instant.now());
                if (mobile != null) {
                    t3.setDeviceId(mobile.getDeviceId());
                    t3.setDeviceType(mobile.getDeviceType());
                    t3.setDeviceIpAddress(mobile.getIpAddress());
                }
                transactionRepository.save(t3);

                Transaction t4 = new Transaction();
                t4.setFromAccount(accountB.getAccountNumber());
                t4.setToAccount(accountA.getAccountNumber());
                t4.setAmount(25.0);
                t4.setTimestamp(java.time.Instant.now());
                if (tablet != null) {
                    t4.setDeviceId(tablet.getDeviceId());
                    t4.setDeviceType(tablet.getDeviceType());
                    t4.setDeviceIpAddress(tablet.getIpAddress());
                }
                transactionRepository.save(t4);

                Transaction t5 = new Transaction();
                t5.setFromAccount(accountA.getAccountNumber());
                t5.setToAccount(accountB.getAccountNumber());
                t5.setAmount(150.0);
                t5.setTimestamp(java.time.Instant.now());
                if (mobile != null) {
                    t5.setDeviceId(mobile.getDeviceId());
                    t5.setDeviceType(mobile.getDeviceType());
                    t5.setDeviceIpAddress(mobile.getIpAddress());
                }
                transactionRepository.save(t5);

                accountA.setBalance(750.0);
                accountB.setBalance(750.0);
                accountRepository.save(accountA);
                accountRepository.save(accountB);
            }
        };
    }
}

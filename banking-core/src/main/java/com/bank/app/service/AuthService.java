package com.bank.app.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    public String login(Map<String, String> credentials) {
        String username = credentials.get("username");
        return "User " + username + " authenticated successfully";
    }
}

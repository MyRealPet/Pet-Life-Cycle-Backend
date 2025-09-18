package com.example.petlifecycle.auth.service;

public interface AuthService {
    Long getAccountIdFromToken(String authorizedHeader);
}

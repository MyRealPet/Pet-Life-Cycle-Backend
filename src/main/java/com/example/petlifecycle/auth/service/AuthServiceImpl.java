package com.example.petlifecycle.auth.service;

import com.example.petlifecycle.redis_cache.RedisCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RedisCacheService redisCacheService;

    @Override
    public Long getAccountIdFromToken(String authorizedHeader) {
        if (authorizedHeader == null || !authorizedHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("유효하지 않은 authorizedHeader 입니다.");
        }
        String userToken = authorizedHeader.replace("Bearer ", "");
        return redisCacheService.getValueByKey(userToken, Long.class);
    }
}

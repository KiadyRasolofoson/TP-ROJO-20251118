package com.notes.apis.service;

import com.notes.apis.model.Token;
import com.notes.apis.repository.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token createToken(Integer ownerId, int daysValid) {
        String value = UUID.randomUUID().toString();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        java.time.LocalDateTime expires = daysValid > 0 ? now.plus(daysValid, java.time.temporal.ChronoUnit.DAYS) : null;
        Token token = new Token(value, ownerId, now, expires);
        return tokenRepository.save(token);
    }

    public boolean isValid(String value) {
        if (value == null || value.isBlank()) return false;
        Optional<Token> t = tokenRepository.findByValue(value);
        if (t.isEmpty()) return false;
        Token token = t.get();
        java.time.LocalDateTime exp = token.getExpiresAt();
        if (exp == null) return true;
        return java.time.LocalDateTime.now().isBefore(exp);
    }

    public void revoke(String value) {
        tokenRepository.deleteByValue(value);
    }

    public Optional<Token> findByValue(String value) {
        return tokenRepository.findByValue(value);
    }

    public Integer getOwnerId(String value) {
        return tokenRepository.findByValue(value).map(Token::getOwnerId).orElse(null);
    }
}

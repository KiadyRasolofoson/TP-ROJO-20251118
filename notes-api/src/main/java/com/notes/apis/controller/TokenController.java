package com.notes.apis.controller;

import com.notes.apis.model.Token;
import com.notes.apis.service.TokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tokens")
public class TokenController {

    private final TokenService tokenService;

    @Value("${app.admin.key:sylviokiady}")
    private String adminKey;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<?> createToken(@RequestHeader(value = "X-ADMIN-KEY", required = false) String key,
                                         @RequestBody Map<String, Object> body) {
        if (adminKey == null || !adminKey.equals(key)) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "admin key required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }

        Integer ownerId = null;
        Object o = body.get("ownerId");
        if (o instanceof Number) ownerId = ((Number) o).intValue();
        else if (o instanceof String) {
            try { ownerId = Integer.parseInt((String) o); } catch (NumberFormatException ignored) {}
        }
        if (ownerId == null) ownerId = -1;
        int days = 30;
        Object d = body.get("daysValid");
        if (d instanceof Number) days = ((Number) d).intValue();
        else if (d instanceof String) {
            try { days = Integer.parseInt((String) d); } catch (NumberFormatException ignored) {}
        }

        Token token = tokenService.createToken(ownerId, days);
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token.getValue());
        resp.put("ownerId", token.getOwnerId());
        resp.put("expiresAt", token.getExpiresAt());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/validate")
    public Map<String, Object> validate(@RequestParam("token") String token) {
        Map<String, Object> m = new HashMap<>();
        m.put("token", token);
        m.put("valid", tokenService.isValid(token));
        return m;
    }

    @DeleteMapping("/{value}")
    public ResponseEntity<?> revoke(@RequestHeader(value = "X-ADMIN-KEY", required = false) String key,
                                    @PathVariable("value") String value) {
        if (adminKey == null || !adminKey.equals(key)) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "admin key required");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }
        tokenService.revoke(value);
        return ResponseEntity.noContent().build();
    }
}

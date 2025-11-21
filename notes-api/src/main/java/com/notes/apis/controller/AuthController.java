package com.notes.apis.controller;

import com.notes.apis.model.Token;
import com.notes.apis.model.User;
import com.notes.apis.service.TokenService;
import com.notes.apis.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        if (username == null || password == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "username and password required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
        Optional<User> existing = userService.findByUsername(username);
        if (existing.isPresent()) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(err);
        }
        User u = userService.createUser(username, password);
        Map<String, Object> resp = new HashMap<>();
        resp.put("id", u.getIdUser());
        resp.put("username", u.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        String username = (String) body.get("username");
        String password = (String) body.get("password");
        if (username == null || password == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "username and password required");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
        }
        Optional<User> uOpt = userService.findByUsername(username);
        if (uOpt.isEmpty() || !userService.checkPassword(uOpt.get(), password)) {
            Map<String, Object> err = new HashMap<>();
            err.put("error", "invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(err);
        }
        User u = uOpt.get();
        int days = 30;
        Object d = body.get("daysValid");
        if (d instanceof Number) days = ((Number) d).intValue();
        else if (d instanceof String) {
            try { days = Integer.parseInt((String) d); } catch (NumberFormatException ignored) {}
        }
        Token token = tokenService.createToken(u.getIdUser(), days);
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token.getValue());
        resp.put("expiresAt", token.getExpiresAt());
        return ResponseEntity.ok(resp);
    }
}

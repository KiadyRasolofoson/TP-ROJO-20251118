package com.notes.apis.security;

import com.notes.apis.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ApiTokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    public ApiTokenFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        // allow token creation & validation endpoints without token
        if (path.startsWith("/api/tokens")) return true;
        // allow auth endpoints (register/login)
        if (path.startsWith("/api/auth")) return true;
        if (path.startsWith("/error")) return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String auth = request.getHeader("Authorization");
        String token = null;
        if (auth != null && auth.startsWith("Bearer ")) {
            token = auth.substring(7);
        }
        if (token == null) token = request.getHeader("X-API-KEY");

        if (token == null || !tokenService.isValid(token)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            Map<String, Object> err = new HashMap<>();
            err.put("error", "invalid or missing API token");
            new ObjectMapper().writeValue(response.getWriter(), err);
            return;
        }
        // token valid -> attach authenticated user id to request
        Integer ownerId = tokenService.getOwnerId(token);
        if (ownerId != null) {
            request.setAttribute("authenticatedUserId", ownerId);
        }

        // token valid -> continue
        filterChain.doFilter(request, response);
    }
}

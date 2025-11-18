package com.notes.apis.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping
    public Map<String, Object> test() {
        Map<String, Object> json = new HashMap<>();
        json.put("message", "API is working!");
        json.put("status", "success");
        json.put("timestamp", System.currentTimeMillis());

        return json;
    }
}

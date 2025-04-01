package com.example.app.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3001")
public class ApiController {

    @PostMapping("/data")
    public ResponseEntity<Map<String, String>> receiveData(@RequestBody Map<String, String> payload) {
        System.out.println("受信データ: " + payload.get("message"));
        Map<String, String> response = new HashMap<>();
        Random random = new Random();
        int number = random.nextInt(8);
        int color = random.nextInt(3);

        response.put("number", String.valueOf(number));
        response.put("color", String.valueOf(color));
        return ResponseEntity.ok(response);  // JSON形式でレスポンスを返す
    }
}


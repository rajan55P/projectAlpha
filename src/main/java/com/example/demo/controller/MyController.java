package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Allow specific origin
public class MyController {
    @GetMapping("/test")
    public ResponseEntity<?> myEndpoint(){
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello World!");
        System.out.println("Inside the method");
        return ResponseEntity.status(HttpStatus.OK).body(response); // Returning a JSON response
    }
}

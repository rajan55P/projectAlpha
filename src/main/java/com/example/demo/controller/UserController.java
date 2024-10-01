package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping("/api/users") // Define your endpoint
    public List<Customer> getUsers() {
        List<Customer> customers = userRepository.findAll();
        return customers;
    }

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUsers(@RequestBody Customer requestUser) {
        // Register users into the database
        System.out.println("user details ="+requestUser+
                "username = "+requestUser.getName() +
                "password = "+requestUser.getPassword());


        Optional<Customer> exitingUser = userRepository.findByName(requestUser.getName());
        if(!exitingUser.isEmpty())
        {
            System.out.println("User Already exist with the username = "+requestUser.getName());
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(requestUser);
        }
        Customer newUser = userDetailsServiceImpl.registerUser(requestUser);
        System.out.println("New User created with the name = "+requestUser.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }
}

package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @GetMapping("/api/users") // Define your endpoint
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @PostMapping("/api/register")
    public User registerUsers(@RequestBody User user) {
        // Register users into the database
        userDetailsServiceImpl.registerUser(user);
        return user;
    }
}

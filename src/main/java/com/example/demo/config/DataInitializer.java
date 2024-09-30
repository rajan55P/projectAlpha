package com.example.demo.config;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    @Lazy
//    private PasswordEncoder passwordEncoder;
//
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
//        User testUser = new User();
//        testUser.setUsername("testuser");
//        testUser.setPassword(passwordEncoder.encode("password"));
//        userRepository.save(testUser);
    }
}

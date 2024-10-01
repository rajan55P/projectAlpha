package com.example.demo.controller;

import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.JwtResponse;
import com.example.demo.model.Customer;
import com.example.demo.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.UserDetailsServiceImpl;

@RestController
public class AuthenticationController {

    private AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsServiceImpl UserDetailsServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            System.out.println("authenticationRequest ="+authenticationRequest+
            "email = "+authenticationRequest.getEmail() +
            "password = "+authenticationRequest.getPassword());
//            try {
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
//                );
//            } catch (Exception e) {
//                throw new Exception("Invalid credentials");
//            }

            final Customer customerDetails = UserDetailsServiceImpl.loadUserByEmail(authenticationRequest.getEmail());
            System.out.println("User details ="+ customerDetails);
            if(customerDetails !=null){
                final String jwt = jwtUtil.generateToken(customerDetails.getEmail());
                System.out.println("Jwt ="+ jwt);
                JwtResponse response = new JwtResponse(jwt);

                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad request");
            }
        } catch (Exception e) {
            System.out.println("Error = " + e);
            throw new Exception("Invalid credentials error =" + e);
        }
    }
}


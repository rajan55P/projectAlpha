package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // Optional: specify the table name if different
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String password;

    private String email;

    // Default constructor
    public Customer() {}

    // Parameterized constructor
    public Customer(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
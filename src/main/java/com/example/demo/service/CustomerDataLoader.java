package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomerDataLoader implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Checking if test customers need to be generated...");

        // Check if any customer whose name starts with 'TestCustomer' exists
        List<Customer> existingTestCustomers = customerRepository.findByNameStartingWith("testcustomer");

        if (existingTestCustomers.isEmpty()) {  // If no test customers exist
            System.out.println("No test customers found, generating and saving test customers...");

            // Create a list of test customers
            List<Customer> testCustomers = new ArrayList<>();

            for (int i = 1; i <= 2; i++) {  // Generate 100 test customers
                Customer customer = new Customer();
                customer.setName("testcustomer" + i);
                customer.setEmail("testcustomer" + i + "@example.com");
                customer.setPassword("testcustomer" + i);  // Use a default password for testing

                testCustomers.add(customer);
            }

            // Save all the generated test customers to the database
            customerRepository.saveAll(testCustomers);
            System.out.println("Test customers saved successfully.");
        } else {
            System.out.println("Test customers already exist, skipping test data generation.");
        }
    }
}

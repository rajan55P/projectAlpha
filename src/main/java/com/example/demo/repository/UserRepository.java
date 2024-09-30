package com.example.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.demo.model.User; // Import the User model

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

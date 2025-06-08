package com.culturalspace.userprofileservice.service;

import com.culturalspace.userprofileservice.model.User;
import com.culturalspace.userprofileservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <-- IMPORTANT: Add this import

import java.util.List;
import java.util.Optional;

@Service // Marks this class as a Spring Service component
public class UserService {

    private final UserRepository userRepository; // Inject the repository

    // Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional // Ensures the entire method runs within a single transaction
    public User createUser(User user) {
        System.out.println("UserService: Creating user: " + user.getUsername());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true) // Optimize read operations
    public Optional<User> getUserById(Long id) {
        System.out.println("UserService: Fetching user with ID: " + id);
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true) // Optimize read operations
    public List<User> getAllUsers() {
        System.out.println("UserService: Fetching all users.");
        return userRepository.findAll();
    }

    @Transactional // Ensures the entire method runs within a single transaction
    public User updateUser(Long id, User updatedUser) {
        System.out.println("UserService: Updating user with ID: " + id);
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPassword(updatedUser.getPassword()); // Remember to hash passwords!
                    existingUser.setRole(updatedUser.getRole());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id)); // Or custom exception
    }

    @Transactional // Ensures the entire method runs within a single transaction
    public void deleteUser(Long id) {
        System.out.println("UserService: Deleting user with ID: " + id);
        userRepository.deleteById(id);
    }
}
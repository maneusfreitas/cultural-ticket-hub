// user-profile-service/src/main/java/com/culturalspace/userprofile/controller/UserController.java
package com.culturalspace.userprofileservice.controller;

import com.culturalspace.userprofileservice.model.User;
import com.culturalspace.userprofileservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks this class as a REST controller
@RequestMapping("/users") // This sets the base path for all user-related endpoints
public class UserController {

    private final UserService userService; // Inject the service

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- MODIFIED ENDPOINTS ---

    // Handles POST requests to /users/create-user
    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED); // Returns 201 Created status
    }

    // Handles GET requests to /users/{id} - No change needed here if you want to keep it this way
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok) // If user found, return 200 OK with user data
                .orElse(ResponseEntity.notFound().build()); // If not found, return 404 Not Found
    }
    // Handles GET requests to /users/all-users
    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users); // Returns 200 OK with list of users
    }
}
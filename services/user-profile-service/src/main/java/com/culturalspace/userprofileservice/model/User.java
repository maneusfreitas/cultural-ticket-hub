// user-profile-service/src/main/java/com/culturalspace/userprofile/model/User.java
package com.culturalspace.userprofileservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // Import for @Table

@Entity // Marks this class as a JPA entity, mapped to a database table
@Table(name = "app_user") // Good practice to avoid conflict with SQL 'USER' keyword
public class User {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID for new entities
    private Long id;
    private String username;
    private String email;
    private String password; // IMPORTANT: In a real app, this MUST be hashed and never stored plain!
    private String role; // e.g., "CLIENT", "OPERATOR", "ADMIN"

    // --- Constructors ---
    // Default constructor is required by JPA
    public User() {}

    public User(String username, String email, String password, String role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // --- Getters and Setters ---
    // (You can use Alt+Insert or Cmd+N in IntelliJ to generate these)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' + // Do NOT include password in toString for security
                '}';
    }
}
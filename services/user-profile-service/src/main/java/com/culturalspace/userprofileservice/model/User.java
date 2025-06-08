// user-profile-service/src/main/java/com/culturalspace/userprofile/model/User.java
package com.culturalspace.userprofileservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // Import for @Table

import java.util.Objects;

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

    // --- Getters ---
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }

    // --- Setters ---
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, role);
    }
}
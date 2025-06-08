// user-profile-service/src/main/java/com/culturalspace/userprofile/repository/UserRepository.java
package com.culturalspace.userprofileservice.repository;

import com.culturalspace.userprofileservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Import for Optional

@Repository // Marks this interface as a Spring Data Repository component
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository provides methods like save(), findById(), findAll(), deleteById(), etc.

    // You can define custom query methods just by naming them correctly
    Optional<User> findByUsername(String username); // Spring Data JPA will implement this for you
    Optional<User> findByEmail(String email);
}
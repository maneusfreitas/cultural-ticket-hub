// src/main/java/com/culturalspace/notificationservice/repository/NotificationLogRepository.java
package com.culturalspace.notificationservice.repository;

import com.culturalspace.notificationservice.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    // Spring Data JPA automatically provides CRUD operations (save, findById, findAll, etc.)
}
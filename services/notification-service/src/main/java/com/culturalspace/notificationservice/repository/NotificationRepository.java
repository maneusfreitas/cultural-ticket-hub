package com.culturalspace.notificationservice.repository;

import com.culturalspace.notificationservice.model.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationLog, Long> {
    // Spring Data JPA automatically provides CRUD operations (save, findById, findAll, etc.)
}
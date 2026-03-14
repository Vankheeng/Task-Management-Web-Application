package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {
}

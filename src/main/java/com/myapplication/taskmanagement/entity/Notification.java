package com.myapplication.taskmanagement.entity;

import com.myapplication.taskmanagement.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    NotificationType type;
    String message;
    Boolean isRead;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

    @ManyToOne
    @JoinColumn(name = "taskId")
    Task task;

    @ManyToOne
    @JoinColumn(name = "projectId")
    Project project;
}

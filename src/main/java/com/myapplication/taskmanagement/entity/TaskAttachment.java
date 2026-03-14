package com.myapplication.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class TaskAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String fileName;
    String fileUrl;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name="createdBy")
    User createdBy;

    @ManyToOne
    @JoinColumn(name="taskId")
    Task task;
}

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
public class TaskAssignment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name="userId")
    User user;

    @ManyToOne
    @JoinColumn(name="taskId")
    Task task;
}

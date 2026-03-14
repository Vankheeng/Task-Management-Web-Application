package com.myapplication.taskmanagement.entity;

import com.myapplication.taskmanagement.enums.Priority;
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
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String title;
    String description;
    Priority priority;
    LocalDate deadline;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name="statusId")
    Status status;
}

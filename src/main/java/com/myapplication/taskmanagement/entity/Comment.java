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
public class Comment extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String content;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name="createdBy")
    User createdBy;

    @ManyToOne
    @JoinColumn(name="taskId")
    Task task;

}

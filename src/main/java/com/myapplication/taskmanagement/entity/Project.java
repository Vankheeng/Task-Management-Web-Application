package com.myapplication.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String description;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name="teamId")
    Team team;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    Set<TaskList> taskLists;
}

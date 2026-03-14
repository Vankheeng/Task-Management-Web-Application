package com.myapplication.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class List {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;

    @ManyToOne
    @JoinColumn(name="projectId")
    Project project;

}

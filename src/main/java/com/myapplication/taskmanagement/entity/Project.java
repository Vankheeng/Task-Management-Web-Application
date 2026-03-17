package com.myapplication.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class Project extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    String id;
    String name;
    String description;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name="teamId")
    Team team;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    Set<TaskList> taskLists;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
    Set<Status> statuses;
}

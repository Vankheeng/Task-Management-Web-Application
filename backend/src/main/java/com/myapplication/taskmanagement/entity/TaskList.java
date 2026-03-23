package com.myapplication.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // thêm dòng này
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskList extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include  // thêm dòng này
    String id;
    String name;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "projectId")
    Project project;

    @OneToMany(mappedBy = "taskList", fetch = FetchType.LAZY)
    @SQLRestriction("active = 1")
    Set<Task> tasks;
}
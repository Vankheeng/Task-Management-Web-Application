package com.myapplication.taskmanagement.entity;

import com.myapplication.taskmanagement.enums.Priority;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level =  AccessLevel.PRIVATE)
public class Task extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    String id;
    String title;
    String description;
    Priority priority;
    LocalDate deadline;
    LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "statusId")
    Status status;

    @ManyToOne
    @JoinColumn(name = "taskListId")
    TaskList taskList;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    @SQLRestriction("active = 1")
    Set<TaskAssignment> taskAssignments;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    @SQLRestriction("active = 1")
    Set<TaskAttachment> taskAttachments;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    @SQLRestriction("active = 1")
    Set<Comment> comments;

}

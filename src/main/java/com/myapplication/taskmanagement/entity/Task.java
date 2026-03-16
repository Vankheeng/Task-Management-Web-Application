package com.myapplication.taskmanagement.entity;

import com.myapplication.taskmanagement.enums.Priority;
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
public class Task extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
    Set<TaskAssignment> taskAssignments;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    Set<TaskAttachment> taskAttachments;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    Set<Comment> comments;

}

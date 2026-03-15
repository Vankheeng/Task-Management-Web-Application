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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)  // thêm dòng này
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include  // chỉ dùng id để so sánh
    String id;
    String name;
    LocalDate createdAt;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    Set<TeamMember> teamMembers;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    Set<Project> projects;
}

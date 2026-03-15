package com.myapplication.taskmanagement.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myapplication.taskmanagement.enums.Role;
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
public class TeamMember {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name="userId")
    @JsonIgnoreProperties({"teamMembers", "projects"})
    private User user;

    @ManyToOne
    @JoinColumn(name="teamId")
    @JsonIgnoreProperties({"teamMembers", "projects"})
    private Team team;

    @Enumerated(EnumType.STRING)
    Role role;

}

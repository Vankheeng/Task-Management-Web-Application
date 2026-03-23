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
public class TeamMember extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    @ManyToOne
    @JoinColumn(name="teamId")
    private Team team;

    @Enumerated(EnumType.STRING)
    Role role;

}

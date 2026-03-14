package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.Team;
import com.myapplication.taskmanagement.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {
}

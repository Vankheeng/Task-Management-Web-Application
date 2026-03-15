package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {
    List<TeamMember> findAllByTeamId(String teamId);
    List<TeamMember> findAllByUserId(String userId);

}
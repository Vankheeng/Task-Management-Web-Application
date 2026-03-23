package com.myapplication.taskmanagement.repository;

import com.myapplication.taskmanagement.entity.TeamMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, String> {
    @EntityGraph(attributePaths = {"team.teamMembers", "team.teamMembers.user"})
    List<TeamMember> findAllByUserIdAndActive(String userId, boolean active);

    List<TeamMember> findAllByTeamIdAndActive(String teamId, boolean active);
    Optional<TeamMember> findByIdAndActive(String id, boolean active);
    boolean existsByTeam_IdAndUser_IdAndActive(String teamId, String userId, boolean active);

    Optional<TeamMember> findByTeamIdAndUserIdAndActive(String teamId, String userId, boolean active);
    @Modifying
    @Query("UPDATE TeamMember tm SET tm.active = false WHERE tm.team.id = :teamId")
    void deactivateAllByTeamId(@Param("teamId") String teamId);


}
package com.myapplication.taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myapplication.taskmanagement.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}

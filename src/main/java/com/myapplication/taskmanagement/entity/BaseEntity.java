package com.myapplication.taskmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter

public abstract class BaseEntity {

    @Column(nullable = false)
    boolean active = true;
}
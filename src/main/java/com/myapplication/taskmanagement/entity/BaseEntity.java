package com.myapplication.taskmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 1")
    boolean active;

    @PrePersist
    public void prePersist(){
        active = true;
    }
}
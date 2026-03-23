package com.myapplication.taskmanagement.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken extends BaseEntity{
    @Id
    @EqualsAndHashCode.Include
    String id;

    Date expiryTime;
}

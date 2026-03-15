package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.NotificationRequest;
import com.myapplication.taskmanagement.dto.request.TeamRequest;
import com.myapplication.taskmanagement.dto.response.NotificationResponse;
import com.myapplication.taskmanagement.dto.response.TeamResponse;
import com.myapplication.taskmanagement.entity.Notification;
import com.myapplication.taskmanagement.entity.Team;
import com.myapplication.taskmanagement.repository.TeamRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "project", ignore = true)
    Notification toNotification(NotificationRequest request);
    NotificationResponse toNotificationResponse(Notification notification);
}

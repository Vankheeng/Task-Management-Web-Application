package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.request.NotificationRequest;
import com.myapplication.taskmanagement.dto.request.TeamRequest;
import com.myapplication.taskmanagement.dto.response.NotificationResponse;
import com.myapplication.taskmanagement.dto.response.TeamResponse;
import com.myapplication.taskmanagement.entity.Notification;
import com.myapplication.taskmanagement.entity.Team;
import com.myapplication.taskmanagement.repository.TeamRepository;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationRequest request);
    NotificationResponse toNotificationResponse(Notification notification);
}

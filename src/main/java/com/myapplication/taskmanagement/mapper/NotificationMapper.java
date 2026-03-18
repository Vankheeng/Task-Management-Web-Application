package com.myapplication.taskmanagement.mapper;

import com.myapplication.taskmanagement.dto.response.NotificationResponse;
import com.myapplication.taskmanagement.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);
}

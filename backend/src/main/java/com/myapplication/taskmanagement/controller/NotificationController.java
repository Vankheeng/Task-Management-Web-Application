package com.myapplication.taskmanagement.controller;

import com.myapplication.taskmanagement.dto.response.APIResponse;
import com.myapplication.taskmanagement.dto.response.NotificationResponse;
import com.myapplication.taskmanagement.dto.response.PageResponse;
import com.myapplication.taskmanagement.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    NotificationService notificationService;

    @GetMapping
    APIResponse<PageResponse<NotificationResponse>> getMyNotifications(
            @RequestParam(required = false) Boolean isRead,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return APIResponse.<PageResponse<NotificationResponse>>builder()
                .result(notificationService.getMyNotifications(isRead, page, size))
                .build();
    }

    @PutMapping("/read/{notificationId}")
    APIResponse<NotificationResponse> markAsRead(@PathVariable String notificationId){
        return APIResponse.<NotificationResponse>builder()
                .result(notificationService.markAsRead(notificationId))
                .build();
    }

    @PutMapping("/read-all")
    APIResponse<String> markAllAsRead(){
        return APIResponse.<String>builder()
                .result(notificationService.markAllAsRead())
                .build();
    }

    @DeleteMapping("/{notificationId}")
    APIResponse<String> deleteNotification(@PathVariable String notificationId){
        return APIResponse.<String>builder()
                .result(notificationService.deleteNotification(notificationId))
                .build();
    }
}
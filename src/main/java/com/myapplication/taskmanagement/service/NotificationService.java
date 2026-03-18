package com.myapplication.taskmanagement.service;

import com.myapplication.taskmanagement.dto.response.NotificationResponse;
import com.myapplication.taskmanagement.dto.response.PageResponse;
import com.myapplication.taskmanagement.entity.*;
import com.myapplication.taskmanagement.enums.NotificationType;
import com.myapplication.taskmanagement.exception.AppException;
import com.myapplication.taskmanagement.exception.ErrorCode;
import com.myapplication.taskmanagement.mapper.NotificationMapper;
import com.myapplication.taskmanagement.repository.NotificationRepository;
import com.myapplication.taskmanagement.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;

    public void notifyTeamAdded(User user, Team team){
        save(Notification.builder()
                .type(NotificationType.TEAM_ADDED)
                .message("You have been added to team: " + team.getName())
                .isRead(false)
                .createdAt(LocalDate.now())
                .user(user)
                .team(team)
                .build());
    }

    public void notifyTaskComment(User user, Task task, String commenterName){
        save(Notification.builder()
                .type(NotificationType.TASK_COMMENT)
                .message(commenterName + " commented on task: " + task.getTitle())
                .isRead(false)
                .createdAt(LocalDate.now())
                .user(user)
                .task(task)
                .build());
    }


    public void notifyTaskStatusUpdated(User user, Task task, String newStatusName){
        save(Notification.builder()
                .type(NotificationType.TASK_STATUS_UPDATED)
                .message("Task '" + task.getTitle() + "' status has been changed to '" + newStatusName + "'")
                .isRead(false)
                .createdAt(LocalDate.now())
                .user(user)
                .task(task)
                .build());
    }

    public void notifyTaskAssigned(User user, Task task){
        save(Notification.builder()
                .type(NotificationType.TASK_ASSIGNED)
                .message("You have been assigned to task: '" + task.getTitle() + "'")
                .isRead(false)
                .createdAt(LocalDate.now())
                .user(user)
                .task(task)
                .build());
    }

    public void notifyTaskUnassigned(User user, Task task){
        save(Notification.builder()
                .type(NotificationType.TASK_UNASSIGNED)
                .message("You have been unassigned from task: '" + task.getTitle() + "'")
                .isRead(false)
                .createdAt(LocalDate.now())
                .user(user)
                .task(task)
                .build());
    }

    private void save(Notification notification){
        notificationRepository.save(notification);
    }

    public void notifyTaskOverdue(User user, Task task){
        if(notificationRepository.existsByTask_IdAndUser_IdAndType(
                task.getId(), user.getId(), NotificationType.TASK_OVERDUE)){
            return;
        }

        save(Notification.builder()
                .type(NotificationType.TASK_OVERDUE)
                .message("Task overdue: " + task.getTitle() +
                        " (deadline: " + task.getDeadline() + ")")
                .isRead(false)
                .createdAt(LocalDate.now())
                .user(user)
                .task(task)
                .build());
    }

    public PageResponse<NotificationResponse> getMyNotifications(
            Boolean isRead, int page, int size){
        String userId = SecurityUtils.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Notification> pageData;
        if(isRead == null){
            pageData = notificationRepository
                    .findAllByUser_IdAndActive(userId, true, pageable);
        } else {
            pageData = notificationRepository
                    .findAllByUser_IdAndIsReadAndActive(userId, isRead, true, pageable);
        }

        return PageResponse.<NotificationResponse>builder()
                .content(pageData.getContent().stream()
                        .map(notificationMapper::toNotificationResponse)
                        .toList())
                .pageNumber(pageData.getNumber())
                .pageSize(pageData.getSize())
                .totalElements(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .last(pageData.isLast())
                .build();
    }

    public NotificationResponse markAsRead(String notificationId){
        Notification notification = notificationRepository
                .findByIdAndActive(notificationId, true)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));

        String userId = SecurityUtils.getCurrentUserId();
        if(!notification.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        notification.setIsRead(true);
        return notificationMapper.toNotificationResponse(notificationRepository.save(notification));
    }

    public String markAllAsRead(){
        String userId = SecurityUtils.getCurrentUserId();
        notificationRepository.findAllByUser_IdAndIsReadAndActive(userId, false, true)
                .forEach(n -> {
                    n.setIsRead(true);
                    notificationRepository.save(n);
                });
        return "All notifications marked as read";
    }

    public String deleteNotification(String notificationId){
        Notification notification = notificationRepository
                .findByIdAndActive(notificationId, true)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));

        String userId = SecurityUtils.getCurrentUserId();
        if(!notification.getUser().getId().equals(userId)){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        notification.setActive(false);
        notificationRepository.save(notification);

        return "Notification deleted successfully";
    }
}
package com.myapplication.taskmanagement.scheduler;

import com.myapplication.taskmanagement.entity.Task;
import com.myapplication.taskmanagement.entity.TaskAssignment;
import com.myapplication.taskmanagement.repository.TaskRepository;
import com.myapplication.taskmanagement.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeadlineScheduler {
    TaskRepository taskRepository;
    NotificationService notificationService;

    @Scheduled(cron = "0 0 8 * * *")
    public void notifyOverdueTasks(){
        List<Task> overdueTasks = taskRepository
                .findAllByDeadlineBeforeAndActive(LocalDate.now(), true);

        overdueTasks.forEach(task ->
                task.getTaskAssignments().stream()
                        .filter(TaskAssignment::isActive)
                        .forEach(ta ->
                                notificationService.notifyTaskOverdue(ta.getUser(), task)
                        )
        );
    }
}
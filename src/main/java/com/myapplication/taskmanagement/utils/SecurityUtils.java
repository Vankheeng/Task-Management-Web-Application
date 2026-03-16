package com.myapplication.taskmanagement.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public static String getCurrentUserId() {
        var context = SecurityContextHolder.getContext();
        return context.getAuthentication().getName();
    }
}
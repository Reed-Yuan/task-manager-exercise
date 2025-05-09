package com.taskmanager.model;

import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Task {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Priority priority;
    private LocalDateTime createdAt;

    public Task() {
        this.createdAt = LocalDateTime.now();
        this.completed = false;
    }

    public Task(String title, String description, Priority priority) {
        this();
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Date getCreatedAtLocal() {
        return Date.from(createdAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
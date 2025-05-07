package com.taskmanager.service;

import com.taskmanager.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task addTask(Task task);
    List<Task> getAllTasks();
    Optional<Task> getTaskById(Long id);
    Task updateTask(Task task);
    void deleteTask(Long id);
    void markTaskAsCompleted(Long id);
    List<Task> getTasksByStatus(boolean completed);
} 
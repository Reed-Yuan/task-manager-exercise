package com.taskmanager.service;

import com.taskmanager.model.Task;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final List<Task> tasks = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Task addTask(Task task) {
        task.setId(idGenerator.getAndIncrement());
        tasks.add(task);
        return task;
    }

    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    @Override
    public Task updateTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(task.getId())) {
                tasks.set(i, task);
                return task;
            }
        }
        return null;
    }

    @Override
    public void deleteTask(Long id) {
        tasks.removeIf(task -> task.getId().equals(id));
    }

    @Override
    public void markTaskAsCompleted(Long id) {
        getTaskById(id).ifPresent(task -> {
            task.setCompleted(true);
            updateTask(task);
        });
    }

    @Override
    public List<Task> getTasksByStatus(boolean completed) {
        return tasks.stream()
                .filter(task -> task.isCompleted() == completed)
                .collect(Collectors.toList());
    }
} 
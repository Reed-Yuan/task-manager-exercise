package com.taskmanager.service.impl;

import com.taskmanager.model.Task;
import com.taskmanager.model.Priority;
import com.taskmanager.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Task> taskRowMapper = new RowMapper<Task>() {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setCompleted(rs.getBoolean("completed"));
            String priorityStr = rs.getString("priority");
            task.setPriority(priorityStr != null ? Priority.valueOf(priorityStr) : null);
            task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return task;
        }
    };

    @Autowired
    public TaskServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("TaskServiceImpl initialized with JdbcTemplate");
    }

    @Override
    public Task addTask(Task task) {
        logger.debug("Adding new task: {}", task.getTitle());
        jdbcTemplate.update(
            "INSERT INTO tasks (title, description, completed, priority) VALUES (?, ?, ?, ?)",
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getPriority() != null ? task.getPriority().name() : null
        );
        
        Task savedTask = jdbcTemplate.queryForObject(
            "SELECT * FROM tasks WHERE id = (SELECT MAX(id) FROM tasks)",
            taskRowMapper
        );
        logger.info("Task added successfully with ID: {}", savedTask.getId());
        return savedTask;
    }

    @Override
    public List<Task> getAllTasks() {
        logger.debug("Retrieving all tasks");
        List<Task> tasks = jdbcTemplate.query("SELECT * FROM tasks ORDER BY created_at DESC", taskRowMapper);
        logger.debug("Retrieved {} tasks", tasks.size());
        return tasks;
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        logger.debug("Retrieving task with ID: {}", id);
        try {
            Task task = jdbcTemplate.queryForObject(
                "SELECT * FROM tasks WHERE id = ?",
                taskRowMapper,
                id
            );
            logger.debug("Task found: {}", task != null);
            return Optional.ofNullable(task);
        } catch (Exception e) {
            logger.warn("Error retrieving task with ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Task updateTask(Task task) {
        logger.debug("Updating task with ID: {}", task.getId());
        jdbcTemplate.update(
            "UPDATE tasks SET title = ?, description = ?, completed = ?, priority = ? WHERE id = ?",
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getPriority() != null ? task.getPriority().name() : null,
            task.getId()
        );
        Task updatedTask = getTaskById(task.getId()).orElseThrow(() -> {
            logger.error("Task not found after update, ID: {}", task.getId());
            return new RuntimeException("Task not found with id: " + task.getId());
        });
        logger.info("Task updated successfully");
        return updatedTask;
    }

    @Override
    public void deleteTask(Long id) {
        logger.debug("Deleting task with ID: {}", id);
        int rowsAffected = jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
        if (rowsAffected > 0) {
            logger.info("Task deleted successfully");
        } else {
            logger.warn("No task found to delete with ID: {}", id);
        }
    }

    @Override
    public void markTaskAsCompleted(Long id) {
        logger.debug("Marking task as completed, ID: {}", id);
        int rowsAffected = jdbcTemplate.update(
            "UPDATE tasks SET completed = true WHERE id = ?",
            id
        );
        if (rowsAffected > 0) {
            logger.info("Task marked as completed successfully");
        } else {
            logger.warn("No task found to mark as completed, ID: {}", id);
        }
    }

    @Override
    public List<Task> getTasksByStatus(boolean completed) {
        logger.debug("Retrieving tasks with completion status: {}", completed);
        List<Task> tasks = jdbcTemplate.query(
            "SELECT * FROM tasks WHERE completed = ? ORDER BY created_at DESC",
            taskRowMapper,
            completed
        );
        logger.debug("Retrieved {} tasks with completion status: {}", tasks.size(), completed);
        return tasks;
    }
} 
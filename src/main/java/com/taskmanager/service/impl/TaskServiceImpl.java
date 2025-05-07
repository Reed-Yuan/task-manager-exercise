package com.taskmanager.service.impl;

import com.taskmanager.model.Task;
import com.taskmanager.model.Priority;
import com.taskmanager.service.TaskService;
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
    }

    @Override
    public Task addTask(Task task) {
        jdbcTemplate.update(
            "INSERT INTO tasks (title, description, completed, priority) VALUES (?, ?, ?, ?)",
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getPriority() != null ? task.getPriority().name() : null
        );
        
        return jdbcTemplate.queryForObject(
            "SELECT * FROM tasks WHERE id = (SELECT MAX(id) FROM tasks)",
            taskRowMapper
        );
    }

    @Override
    public List<Task> getAllTasks() {
        return jdbcTemplate.query("SELECT * FROM tasks ORDER BY created_at DESC", taskRowMapper);
    }

    @Override
    public Optional<Task> getTaskById(Long id) {
        try {
            Task task = jdbcTemplate.queryForObject(
                "SELECT * FROM tasks WHERE id = ?",
                taskRowMapper,
                id
            );
            return Optional.ofNullable(task);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Task updateTask(Task task) {
        jdbcTemplate.update(
            "UPDATE tasks SET title = ?, description = ?, completed = ?, priority = ? WHERE id = ?",
            task.getTitle(),
            task.getDescription(),
            task.isCompleted(),
            task.getPriority() != null ? task.getPriority().name() : null,
            task.getId()
        );
        return getTaskById(task.getId()).orElseThrow(() -> 
            new RuntimeException("Task not found with id: " + task.getId()));
    }

    @Override
    public void deleteTask(Long id) {
        jdbcTemplate.update("DELETE FROM tasks WHERE id = ?", id);
    }

    @Override
    public void markTaskAsCompleted(Long id) {
        jdbcTemplate.update(
            "UPDATE tasks SET completed = true WHERE id = ?",
            id
        );
    }

    @Override
    public List<Task> getTasksByStatus(boolean completed) {
        return jdbcTemplate.query(
            "SELECT * FROM tasks WHERE completed = ? ORDER BY created_at DESC",
            taskRowMapper,
            completed
        );
    }
} 
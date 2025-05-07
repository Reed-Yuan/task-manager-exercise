package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl();
    }

    @Test
    void whenAddTask_thenTaskIsAdded() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setPriority(Priority.MEDIUM);

        // When
        Task savedTask = taskService.addTask(task);

        // Then
        assertNotNull(savedTask.getId());
        assertEquals("Test Task", savedTask.getTitle());
        assertEquals("Test Description", savedTask.getDescription());
        assertEquals(Priority.MEDIUM, savedTask.getPriority());
        assertFalse(savedTask.isCompleted());
    }

    @Test
    void whenGetAllTasks_thenReturnAllTasks() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");
        taskService.addTask(task1);
        taskService.addTask(task2);

        // When
        List<Task> tasks = taskService.getAllTasks();

        // Then
        assertEquals(2, tasks.size());
        assertThat(tasks, containsInAnyOrder(
            hasProperty("title", is("Task 1")),
            hasProperty("title", is("Task 2"))
        ));
    }

    @Test
    void whenMarkTaskAsCompleted_thenTaskIsCompleted() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        Task savedTask = taskService.addTask(task);
        assertFalse(savedTask.isCompleted());

        // When
        taskService.markTaskAsCompleted(savedTask.getId());

        // Then
        Optional<Task> updatedTask = taskService.getTaskById(savedTask.getId());
        assertTrue(updatedTask.isPresent());
        assertTrue(updatedTask.get().isCompleted());
    }

    @Test
    void whenDeleteTask_thenTaskIsRemoved() {
        // Given
        Task task = new Task();
        task.setTitle("Test Task");
        Task savedTask = taskService.addTask(task);

        // When
        taskService.deleteTask(savedTask.getId());

        // Then
        Optional<Task> deletedTask = taskService.getTaskById(savedTask.getId());
        assertFalse(deletedTask.isPresent());
    }

    @Test
    void whenGetTasksByStatus_thenReturnFilteredTasks() {
        // Given
        Task completedTask = new Task();
        completedTask.setTitle("Completed Task");
        Task savedCompletedTask = taskService.addTask(completedTask);
        taskService.markTaskAsCompleted(savedCompletedTask.getId());

        Task pendingTask = new Task();
        pendingTask.setTitle("Pending Task");
        taskService.addTask(pendingTask);

        // When
        List<Task> completedTasks = taskService.getTasksByStatus(true);
        List<Task> pendingTasks = taskService.getTasksByStatus(false);

        // Then
        assertEquals(1, completedTasks.size());
        assertEquals(1, pendingTasks.size());
        assertTrue(completedTasks.get(0).isCompleted());
        assertFalse(pendingTasks.get(0).isCompleted());
    }

    @Test
    void whenUpdateTask_thenTaskIsUpdated() {
        // Given
        Task task = new Task();
        task.setTitle("Original Title");
        task.setDescription("Original Description");
        task.setPriority(Priority.LOW);
        Task savedTask = taskService.addTask(task);

        // When
        savedTask.setTitle("Updated Title");
        savedTask.setDescription("Updated Description");
        savedTask.setPriority(Priority.HIGH);
        Task updatedTask = taskService.updateTask(savedTask);

        // Then
        assertEquals("Updated Title", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals(Priority.HIGH, updatedTask.getPriority());
    }
} 
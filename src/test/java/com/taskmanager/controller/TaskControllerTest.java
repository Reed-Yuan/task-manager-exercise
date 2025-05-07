package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.model.Priority;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ViewResolver viewResolver = new InternalResourceViewResolver();
        ((InternalResourceViewResolver) viewResolver).setPrefix("/WEB-INF/views/");
        ((InternalResourceViewResolver) viewResolver).setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(taskController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    void whenGetAllTasks_thenReturnTaskList() throws Exception {
        // Given
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");

        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        // When & Then
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks"))
                .andExpect(model().attributeExists("tasks"));

        verify(taskService).getAllTasks();
    }

    @Test
    void whenShowAddTaskForm_thenReturnAddTaskForm() throws Exception {
        mockMvc.perform(get("/tasks/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-task"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    void whenAddTask_thenRedirectToTaskList() throws Exception {
        // Given
        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("Description");
        task.setPriority(Priority.MEDIUM);

        when(taskService.addTask(any(Task.class))).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/tasks")
                .param("title", "New Task")
                .param("description", "Description")
                .param("priority", "MEDIUM"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService).addTask(any(Task.class));
    }

    @Test
    void whenMarkTaskAsCompleted_thenRedirectToTaskList() throws Exception {
        // Given
        Long taskId = 1L;
        doNothing().when(taskService).markTaskAsCompleted(taskId);

        // When & Then
        mockMvc.perform(post("/tasks/{id}/complete", taskId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService).markTaskAsCompleted(taskId);
    }

    @Test
    void whenDeleteTask_thenRedirectToTaskList() throws Exception {
        // Given
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        // When & Then
        mockMvc.perform(post("/tasks/{id}/delete", taskId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));

        verify(taskService).deleteTask(taskId);
    }

    @Test
    void whenFilterTasks_thenReturnFilteredTasks() throws Exception {
        // Given
        Task completedTask = new Task();
        completedTask.setId(1L);
        completedTask.setTitle("Completed Task");
        completedTask.setCompleted(true);

        when(taskService.getTasksByStatus(true))
                .thenReturn(Arrays.asList(completedTask));

        // When & Then
        mockMvc.perform(get("/tasks/filter")
                .param("completed", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks"))
                .andExpect(model().attributeExists("tasks"));

        verify(taskService).getTasksByStatus(true);
    }
} 
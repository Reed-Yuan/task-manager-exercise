package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
        logger.info("TaskController initialized with TaskService");
    }

    @GetMapping
    public String getAllTasks(Model model) {
        logger.debug("Getting all tasks");
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        logger.debug("Retrieved {} tasks", tasks.size());
        return "tasks";
    }

    @PostMapping("/{id}/complete")
    public String markTaskAsCompleted(@PathVariable Long id) {
        logger.info("Marking task as completed, task ID: {}", id);
        taskService.markTaskAsCompleted(id);
        logger.info("Task marked as completed successfully");
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/toggle")
    public String toggleTaskCompletion(@PathVariable Long id) {
        logger.info("Toggling task completion status, task ID: {}", id);
        Optional<Task> taskOpt = taskService.getTaskById(id);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            boolean newStatus = !task.isCompleted();
            logger.debug("Current completion status: {}, new status: {}", task.isCompleted(), newStatus);
            task.setCompleted(newStatus);
            taskService.updateTask(task);
            logger.info("Task completion status toggled successfully");
        } else {
            logger.warn("Task not found with ID: {}", id);
        }
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String deleteTask(@PathVariable Long id) {
        logger.info("Deleting task with ID: {}", id);
        taskService.deleteTask(id);
        logger.info("Task deleted successfully");
        return "redirect:/tasks";
    }

    @GetMapping("/addTask")
    public String showAddTaskForm(Model model) {
        logger.debug("Displaying add task form");
        model.addAttribute("task", new Task()); // Add an empty Task object to the model
        return "/addTask"; // Return the view name for the add task form
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) {
        logger.info("Adding new task: {}", task.getTitle());
        taskService.addTask(task);
        logger.info("Task added successfully with ID: {}", task.getId());
        return "redirect:/tasks";
    }

    @GetMapping("/filter")
    public String filterTasks(@RequestParam boolean completed, Model model) {
        logger.debug("Filtering tasks by completion status: {}", completed);
        List<Task> tasks = taskService.getTasksByStatus(completed);
        model.addAttribute("tasks", tasks);
        logger.debug("Retrieved {} tasks with completion status: {}", tasks.size(), completed);
        return "tasks";
    }
}

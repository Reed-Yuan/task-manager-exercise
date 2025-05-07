<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Task Manager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Task Manager</h1>
        
        <!-- Filter buttons -->
        <div class="filters">
            <a href="${pageContext.request.contextPath}/tasks" class="btn ${empty param.filter ? 'active' : ''}">All</a>
            <a href="${pageContext.request.contextPath}/tasks?filter=active" class="btn ${param.filter == 'active' ? 'active' : ''}">Active</a>
            <a href="${pageContext.request.contextPath}/tasks?filter=completed" class="btn ${param.filter == 'completed' ? 'active' : ''}">Completed</a>
        </div>

        <!-- Add new task form -->
        <div class="add-task">
            <h2>Add New Task</h2>
            <form action="${pageContext.request.contextPath}/tasks" method="post">
                <div class="form-group">
                    <input type="text" name="title" placeholder="Task Title" required>
                </div>
                <div class="form-group">
                    <textarea name="description" placeholder="Task Description" required></textarea>
                </div>
                <div class="form-group">
                    <select name="priority" required>
                        <option value="LOW">Low Priority</option>
                        <option value="MEDIUM">Medium Priority</option>
                        <option value="HIGH">High Priority</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Add Task</button>
            </form>
        </div>

        <!-- Task list -->
        <div class="task-list">
            <h2>Tasks</h2>
            <c:forEach items="${tasks}" var="task">
                <div class="task-item ${task.completed ? 'completed' : ''}">
                    <div class="task-content">
                        <h3>${task.title}</h3>
                        <p>${task.description}</p>
                        <div class="task-meta">
                            <span class="priority ${task.priority.name().toLowerCase()}">${task.priority}</span>
                            <span class="date">${task.createdAt}</span>
                        </div>
                    </div>
                    <div class="task-actions">
                        <form action="${pageContext.request.contextPath}/tasks/${task.id}/toggle" method="post" style="display: inline;">
                            <button type="submit" class="btn btn-small">
                                ${task.completed ? 'Mark Incomplete' : 'Mark Complete'}
                            </button>
                        </form>
                        <form action="${pageContext.request.contextPath}/tasks/${task.id}/delete" method="post" style="display: inline;">
                            <button type="submit" class="btn btn-small btn-danger">Delete</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</body>
</html> 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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

        <!-- Task list as a table -->
        <div class="task-list">
            <h2>Tasks</h2>
            <table class="task-table">
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Priority</th>
                        <th>Created At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${tasks}" var="task">
                        <tr class="${task.completed ? 'completed' : ''}">
                            <td data-label="Title">${task.title}</td>
                            <td data-label="Description">${task.description}</td>
                            <td data-label="Priority"><span class="priority ${task.priority.name().toLowerCase()}">${task.priority}</span></td>
                            <td data-label="Created At">
                                
                                <span class="date">
                                    <fmt:formatDate value="${task.createdAtLocal}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </span>
                            </td>
                            <td data-label="Actions" class="task-actions-cell">
                                <form action="${pageContext.request.contextPath}/tasks/${task.id}/toggle" method="post">
                                    <button type="submit" class="btn btn-small">
                                        ${task.completed ? 'Mark Incomplete' : 'Mark Complete'}
                                    </button>
                                </form>
                                <form action="${pageContext.request.contextPath}/tasks/${task.id}/delete" method="post">
                                    <button type="submit" class="btn btn-small btn-danger">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        
        <!-- Filter buttons -->
        <div class="filters">
            <a href="${pageContext.request.contextPath}/tasks" class="btn ${empty param.completed ? 'active' : ''}">All</a>
            <a href="${pageContext.request.contextPath}/tasks/filter?completed=false" class="btn ${param.completed == 'false' ? 'active' : ''}">Active</a>
            <a href="${pageContext.request.contextPath}/tasks/filter?completed=true" class="btn ${param.completed == 'true' ? 'active' : ''}">Completed</a>
            <a href="${pageContext.request.contextPath}/tasks/addTask" class="btn btn-primary">Add New Task</a>
        </div>
    </div>
</body>
</html>

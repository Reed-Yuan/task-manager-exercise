<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Task</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>
    <div class="container">
        <h1>Add New Task</h1>
        <div class="add-task">
            <form action="${pageContext.request.contextPath}/tasks/add" method="post">
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
    </div>
</body>
</html>

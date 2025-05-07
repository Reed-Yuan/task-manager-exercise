# Task Manager Application

A Spring MVC-based task management application that allows users to create, view, update, and delete tasks.

## Features

- View list of tasks
- Add new tasks with title, description, and priority
- Mark tasks as complete/incomplete
- Delete tasks
- Filter tasks by status (All, Active, Completed)

## Technology Stack

- Spring MVC 5.3.30
- JSP (JavaServer Pages)
- Maven
- CSS for styling

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- Servlet container (e.g., Tomcat)

## Building and Running

1. Clone the repository
2. Build the project:
   ```bash
   mvn clean package
   ```
3. Run the application using Maven:
   ```bash
   mvn tomcat7:run
   ```
4. Access the application at: `http://localhost:8080/task-manager/tasks`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── taskmanager/
│   │           ├── controller/
│   │           ├── model/
│   │           └── service/
│   └── webapp/
│       ├── WEB-INF/
│       │   ├── views/
│       │   └── spring-mvc.xml
│       └── resources/
│           └── css/
```

## License

This project is licensed under the MIT License. 
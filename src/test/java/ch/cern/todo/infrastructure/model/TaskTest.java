package ch.cern.todo.infrastructure.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskConstructorAndGetters() {
        // Arrange
        Long taskId = 1L;
        String taskName = "Test Task";
        String description = "This is a test task.";
        LocalDateTime deadline = LocalDateTime.now();
        TaskCategory category = new TaskCategory();
        CustomUser user = new CustomUser();

        // Act
        Task task = new Task(taskId, taskName, description, deadline, category, user);

        // Assert
        assertEquals(taskId, task.getId());
        assertEquals(taskName, task.getName());
        assertEquals(description, task.getDescription());
        assertEquals(deadline, task.getDeadline());
        assertEquals(category, task.getCategory());
        assertEquals(user, task.getUser());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Long taskId = 1L;
        String taskName = "Test Task";
        String description = "This is a test task.";
        LocalDateTime deadline = LocalDateTime.now();
        TaskCategory category = new TaskCategory();
        CustomUser user = new CustomUser();

        Task task1 = new Task(taskId, taskName, description, deadline, category, user);
        Task task2 = new Task(taskId, taskName, description, deadline, category, user);

        // Act & Assert
        assertEquals(task1, task2); // Test equals
        assertEquals(task1.hashCode(), task2.hashCode()); // Test hashCode
    }

    @Test
    void testNotEquals() {
        // Arrange
        Task task1 = new Task(1L, "Task 1", "Description 1", LocalDateTime.now(), new TaskCategory(), new CustomUser());
        Task task2 = new Task(2L, "Task 2", "Description 2", LocalDateTime.now().plusDays(1), new TaskCategory(), new CustomUser());

        // Act & Assert
        assertNotEquals(task1, task2);
    }
}
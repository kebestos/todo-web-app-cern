package ch.cern.todo.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskQueryDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        String name = "Test Task";
        String description = "This is a description";
        String deadline = "2025-01-28T15:30:00";
        Long categoryId = 1L;
        Long userId = 2L;
        String deadlineCriteria = "EQUAL";

        // Act
        TaskQueryDTO taskQueryDTO = new TaskQueryDTO(name, description, deadline, categoryId, userId, deadlineCriteria);

        // Assert
        assertEquals(name, taskQueryDTO.getName());
        assertEquals(description, taskQueryDTO.getDescription());
        assertEquals(deadline, taskQueryDTO.getDeadline());
        assertEquals(categoryId, taskQueryDTO.getCategoryId());
        assertEquals(userId, taskQueryDTO.getUserId());
        assertEquals(deadlineCriteria, taskQueryDTO.getDeadlineCriteria());
    }

    @Test
    void testSetters() {
        // Arrange
        TaskQueryDTO taskQueryDTO = new TaskQueryDTO(null, null, null, null, null, null);

        String name = "Updated Task";
        String description = "Updated description";
        String deadline = "2025-01-29T10:00:00";
        Long categoryId = 10L;
        Long userId = 20L;
        String deadlineCriteria = "GREATER";

        // Act
        taskQueryDTO.setName(name);
        taskQueryDTO.setDescription(description);
        taskQueryDTO.setDeadline(deadline);
        taskQueryDTO.setCategoryId(categoryId);
        taskQueryDTO.setUserId(userId);
        taskQueryDTO.setDeadlineCriteria(deadlineCriteria);

        // Assert
        assertEquals(name, taskQueryDTO.getName());
        assertEquals(description, taskQueryDTO.getDescription());
        assertEquals(deadline, taskQueryDTO.getDeadline());
        assertEquals(categoryId, taskQueryDTO.getCategoryId());
        assertEquals(userId, taskQueryDTO.getUserId());
        assertEquals(deadlineCriteria, taskQueryDTO.getDeadlineCriteria());
    }
}
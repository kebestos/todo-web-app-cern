package ch.cern.todo.controller.dto.mapper;

import ch.cern.todo.controller.dto.TaskQueryDTO;
import ch.cern.todo.domain.TaskQuery;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskQueryMapperTest {

    private final TaskQueryMapper mapper = new TaskQueryMapper();

    @Test
    void testToTaskQuery() {
        // Arrange
        String name = "Test Task";
        String description = "This is a test task";
        String deadline = "2025-01-28T15:30:00";
        Long categoryId = 1L;
        Long userId = 2L;
        String deadlineCriteria = "EQUAL";

        TaskQueryDTO taskQueryDto = new TaskQueryDTO(name, description, deadline, categoryId, userId, deadlineCriteria);

        // Act
        TaskQuery taskQuery = mapper.toTaskQuery(taskQueryDto);

        // Assert
        assertNotNull(taskQuery);
        assertEquals(name, taskQuery.name());
        assertEquals(description, taskQuery.description());
        assertEquals(LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), taskQuery.deadline());
        assertEquals(categoryId, taskQuery.categoryId());
        assertEquals(userId, taskQuery.userId());
        assertEquals(deadlineCriteria, taskQuery.deadlineCriteria());
    }

    @Test
    void testToTaskQueryDto() {
        // Arrange
        String name = "Test Task";
        String description = "This is a test task";
        LocalDateTime deadline = LocalDateTime.of(2025, 1, 28, 15, 30);
        Long categoryId = 1L;
        Long userId = 2L;
        String deadlineCriteria = "EQUAL";

        TaskQuery taskQuery = new TaskQuery(name, description, deadline, categoryId, userId, deadlineCriteria);

        // Act
        TaskQueryDTO taskQueryDto = mapper.toTaskQueryDto(taskQuery);

        // Assert
        assertNotNull(taskQueryDto);
        assertEquals(name, taskQueryDto.getName());
        assertEquals(description, taskQueryDto.getDescription());
        assertEquals(deadline.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), taskQueryDto.getDeadline());
        assertEquals(categoryId, taskQueryDto.getCategoryId());
        assertEquals(userId, taskQueryDto.getUserId());
        assertEquals(deadlineCriteria, taskQueryDto.getDeadlineCriteria());
    }
}
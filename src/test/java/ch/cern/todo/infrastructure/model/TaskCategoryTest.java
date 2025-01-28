package ch.cern.todo.infrastructure.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TaskCategoryTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String name = "Test Category";
        String description = "Test Description";

        // Act
        TaskCategory taskCategory = new TaskCategory(id, name, description);

        // Assert
        assertEquals(id, taskCategory.getId());
        assertEquals(name, taskCategory.getName());
        assertEquals(description, taskCategory.getDescription());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        TaskCategory taskCategory1 = new TaskCategory(1L, "Category 1", "Description 1");
        TaskCategory taskCategory2 = new TaskCategory(1L, "Category 1", "Description 1");

        // Act & Assert
        assertEquals(taskCategory1, taskCategory2);
        assertEquals(taskCategory1.hashCode(), taskCategory2.hashCode());
    }

    @Test
    void testNotEquals() {
        // Arrange
        TaskCategory taskCategory1 = new TaskCategory(1L, "Category 1", "Description 1");
        TaskCategory taskCategory2 = new TaskCategory(2L, "Category 2", "Description 2");

        // Act & Assert
        assertNotEquals(taskCategory1, taskCategory2);
    }
}
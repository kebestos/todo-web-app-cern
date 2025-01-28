package ch.cern.todo.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskCategoryDTOTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String name = "Category Name";
        String description = "Category Description";

        // Act
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(id, name, description);

        // Assert
        assertEquals(id, taskCategoryDTO.getId());
        assertEquals(name, taskCategoryDTO.getName());
        assertEquals(description, taskCategoryDTO.getDescription());
    }

    @Test
    void testSetters() {
        // Arrange
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(null, null, null);

        Long id = 2L;
        String name = "Updated Name";
        String description = "Updated Description";

        // Act
        taskCategoryDTO.setId(id);
        taskCategoryDTO.setName(name);
        taskCategoryDTO.setDescription(description);

        // Assert
        assertEquals(id, taskCategoryDTO.getId());
        assertEquals(name, taskCategoryDTO.getName());
        assertEquals(description, taskCategoryDTO.getDescription());
    }
}
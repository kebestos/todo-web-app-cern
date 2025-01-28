package ch.cern.todo.controller.dto.mapper;

import ch.cern.todo.controller.dto.TaskCategoryDTO;
import ch.cern.todo.infrastructure.model.TaskCategory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskCategoryMapperTest {

    private final TaskCategoryMapper mapper = new TaskCategoryMapper();

    @Test
    void testToTaskCategoryDto() {
        // Arrange
        Long id = 1L;
        String name = "Category Name";
        String description = "Category Description";

        TaskCategory taskCategory = new TaskCategory(id, name, description);

        // Act
        TaskCategoryDTO taskCategoryDto = mapper.toTaskCategoryDto(taskCategory);

        // Assert
        assertNotNull(taskCategoryDto);
        assertEquals(id, taskCategoryDto.getId());
        assertEquals(name, taskCategoryDto.getName());
        assertEquals(description, taskCategoryDto.getDescription());
    }

    @Test
    void testToTaskCategory() {
        // Arrange
        Long id = 1L;
        String name = "Category Name";
        String description = "Category Description";

        TaskCategoryDTO taskCategoryDto = new TaskCategoryDTO(id, name, description);

        // Act
        TaskCategory taskCategory = mapper.toTaskCategory(taskCategoryDto);

        // Assert
        assertNotNull(taskCategory);
        assertEquals(id, taskCategory.getId());
        assertEquals(name, taskCategory.getName());
        assertEquals(description, taskCategory.getDescription());
    }
}
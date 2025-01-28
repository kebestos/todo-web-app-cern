package ch.cern.todo.controller.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskDTOTest {

    @Test
    void taskDtoInstantiation() {
        // Arrange
        Long id = 1L;
        String name = "Task";
        String description = "This is a test task";
        String deadline = "2013-04-23T18:25:43";
        TaskCategoryDTO taskCategory = new TaskCategoryDTO(1L, "Dev", "Technical development task");

        // Act
        TaskDTO taskDTO = new TaskDTO(id, name, description, deadline, taskCategory);

        // Assert
        assertEquals(id, taskDTO.getId());
        assertEquals(name, taskDTO.getName());
        assertEquals(description, taskDTO.getDescription());
        assertEquals(deadline, taskDTO.getDeadline());
        assertEquals(taskCategory, taskDTO.getTaskCategory());
    }

    @Test
    void taskDTO_SettersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        TaskDTO taskDTO = new TaskDTO();

        Long id = 1L;
        String name = "Task";
        String description = "This is a test task";
        String deadline = "2013-04-23T18:25:43";
        TaskCategoryDTO taskCategory = new TaskCategoryDTO(1L, "Dev", "Technical development task");

        // Act
        taskDTO.setId(id);
        taskDTO.setName(name);
        taskDTO.setDescription(description);
        taskDTO.setDeadline(deadline);
        taskDTO.setTaskCategory(taskCategory);

        // Assert
        assertEquals(id, taskDTO.getId());
        assertEquals(name, taskDTO.getName());
        assertEquals(description, taskDTO.getDescription());
        assertEquals(deadline, taskDTO.getDeadline());
        assertEquals(taskCategory, taskDTO.getTaskCategory());
    }
}

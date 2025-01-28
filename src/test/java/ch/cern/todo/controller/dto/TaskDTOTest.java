package ch.cern.todo.controller.dto;

import org.junit.jupiter.api.Test;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskDTOTest {

    @Test
    void taskDtoInstantiation() {

        Long id = 1L;
        String name = "Task";
        String description = "This is a test task";
        String deadline = "2013-04-23T18:25:43";
        TaskCategoryDTO taskCategory = new TaskCategoryDTO(1L, "Dev", "Technical development task");

        TaskDTO taskDTO = new TaskDTO(id, name, description, deadline, taskCategory);

        assertEquals(id, taskDTO.getId());
        assertEquals(name, taskDTO.getName());
        assertEquals(description, taskDTO.getDescription());
        assertEquals(deadline, taskDTO.getDeadline());
        assertEquals(taskCategory, taskDTO.getTaskCategory());
    }

    @Test
    void taskDTO_ShouldHaveCorrectDateTimeFormatAnnotation() throws NoSuchFieldException {
        Field deadlineField = TaskDTO.class.getDeclaredField("deadline");
        DateTimeFormat dateTimeFormat = deadlineField.getAnnotation(DateTimeFormat.class);

        assertNotNull(dateTimeFormat, "The @DateTimeFormat annotation should be present on the deadline field.");
        assertEquals("yyyy-MM-dd'T'HH:mm:ss", dateTimeFormat.pattern(),
                "The @DateTimeFormat pattern should match 'yyyy-MM-dd'T'HH:mm:ss'.");
    }

    @Test
    void taskDTO_SettersAndGetters_ShouldWorkCorrectly() {
        TaskDTO taskDTO = new TaskDTO();

        Long id = 1L;
        String name = "Task";
        String description = "This is a test task";
        String deadline = "2013-04-23T18:25:43";
        TaskCategoryDTO taskCategory = new TaskCategoryDTO(1L, "Dev", "Technical development task");

        taskDTO.setId(id);
        taskDTO.setName(name);
        taskDTO.setDescription(description);
        taskDTO.setDeadline(deadline);
        taskDTO.setTaskCategory(taskCategory);

        assertEquals(id, taskDTO.getId());
        assertEquals(name, taskDTO.getName());
        assertEquals(description, taskDTO.getDescription());
        assertEquals(deadline, taskDTO.getDeadline());
        assertEquals(taskCategory, taskDTO.getTaskCategory());
    }
}

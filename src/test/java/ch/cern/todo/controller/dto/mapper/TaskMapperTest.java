package ch.cern.todo.controller.dto.mapper;

import ch.cern.todo.controller.dto.TaskCategoryDTO;
import ch.cern.todo.controller.dto.TaskDTO;
import ch.cern.todo.infrastructure.model.Task;
import ch.cern.todo.infrastructure.model.TaskCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskMapperTest {

    private TaskMapper taskMapper;

    @Mock
    private TaskCategoryMapper taskCategoryMapper;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskMapper = new TaskMapper(taskCategoryMapper);
    }

    @Test
    void toTaskDto_ShouldMapTaskToTaskDTO() {
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(1L, "Dev", "Technical development task");
        TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");

        Task task = new Task(1L, "Task Name", "Task Description",
                LocalDateTime.parse("2025-01-31T10:00:00", DATE_TIME_FORMATTER),
                taskCategory, null);

        when(taskCategoryMapper.toTaskCategoryDto(taskCategory)).thenReturn(taskCategoryDTO);

        // Act
        TaskDTO taskDTO = taskMapper.toTaskDto(task);

        // Assert
        assertNotNull(taskDTO);
        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getName(), taskDTO.getName());
        assertEquals(task.getDescription(), taskDTO.getDescription());
        assertEquals(task.getDeadline().format(DATE_TIME_FORMATTER), taskDTO.getDeadline());
        assertEquals(taskCategoryDTO, taskDTO.getTaskCategory());
        verify(taskCategoryMapper, times(1)).toTaskCategoryDto(taskCategory);
    }

    @Test
    void toTask_ShouldMapTaskDTOToTask() {
        // Arrange
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(1L, "Dev", "Technical development task");
        TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");

        TaskDTO taskDTO = new TaskDTO(1L, "Task Name", "Task Description",
                "2025-01-31T10:00:00", taskCategoryDTO);

        when(taskCategoryMapper.toTaskCategory(taskCategoryDTO)).thenReturn(taskCategory);

        // Act
        Task task = taskMapper.toTask(taskDTO);

        // Assert
        assertNotNull(task);
        assertEquals(taskDTO.getId(), task.getId());
        assertEquals(taskDTO.getName(), task.getName());
        assertEquals(taskDTO.getDescription(), task.getDescription());
        assertEquals(LocalDateTime.parse(taskDTO.getDeadline(), DATE_TIME_FORMATTER), task.getDeadline());
        assertEquals(taskCategory, task.getCategory());
        assertNull(task.getUser());
        verify(taskCategoryMapper, times(1)).toTaskCategory(taskCategoryDTO);
    }

    @Test
    void toTaskDtoList_ShouldMapTaskListToTaskDTOList() {
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(1L, "Dev", "Technical development task");
        TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");

        Task task1 = new Task(1L, "Task 1", "Description 1",
                LocalDateTime.parse("2025-01-31T10:00:00", DATE_TIME_FORMATTER),
                taskCategory, null);
        Task task2 = new Task(2L, "Task 2", "Description 2",
                LocalDateTime.parse("2025-02-01T12:00:00", DATE_TIME_FORMATTER),
                taskCategory, null);

        when(taskCategoryMapper.toTaskCategoryDto(taskCategory)).thenReturn(taskCategoryDTO);

        // Act
        List<TaskDTO> taskDTOList = taskMapper.toTaskDtoList(List.of(task1, task2));

        // Assert
        assertNotNull(taskDTOList);
        assertEquals(2, taskDTOList.size());

        TaskDTO taskDTO1 = taskDTOList.get(0);
        TaskDTO taskDTO2 = taskDTOList.get(1);

        assertEquals(task1.getId(), taskDTO1.getId());
        assertEquals(task1.getName(), taskDTO1.getName());
        assertEquals(task1.getDeadline().format(DATE_TIME_FORMATTER), taskDTO1.getDeadline());

        assertEquals(task2.getId(), taskDTO2.getId());
        assertEquals(task2.getName(), taskDTO2.getName());
        assertEquals(task2.getDeadline().format(DATE_TIME_FORMATTER), taskDTO2.getDeadline());

        verify(taskCategoryMapper, times(2)).toTaskCategoryDto(taskCategory);
    }
}

package ch.cern.todo.controller;

import ch.cern.todo.controller.dto.TaskCategoryDTO;
import ch.cern.todo.controller.dto.mapper.TaskCategoryMapper;
import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.service.TaskCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TaskCategoryControllerTest {

    @InjectMocks
    private TaskCategoryController taskCategoryController;

    @Mock
    private TaskCategoryService taskCategoryService;

    @Mock
    private TaskCategoryMapper taskCategoryMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTaskCategory() {
        // Arrange
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(1L, "Test Category", "Test Description");
        TaskCategory taskCategory = new TaskCategory(1L, "Test Category", "Test Description");

        when(taskCategoryMapper.toTaskCategory(taskCategoryDTO)).thenReturn(taskCategory);
        when(taskCategoryService.createTaskCategory(taskCategory)).thenReturn(taskCategory);
        when(taskCategoryMapper.toTaskCategoryDto(taskCategory)).thenReturn(taskCategoryDTO);

        // Act
        ResponseEntity<TaskCategoryDTO> response = taskCategoryController.createTaskCategory(taskCategoryDTO);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(taskCategoryDTO, response.getBody());

        verify(taskCategoryMapper, times(1)).toTaskCategory(taskCategoryDTO);
        verify(taskCategoryService, times(1)).createTaskCategory(taskCategory);
        verify(taskCategoryMapper, times(1)).toTaskCategoryDto(taskCategory);
    }

    @Test
    void testGetTaskCategoryById() {
        // Arrange
        Long taskCategoryId = 1L;
        TaskCategory taskCategory = new TaskCategory(1L, "Test Category", "Test Description");
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(1L, "Test Category", "Test Description");

        when(taskCategoryService.getTaskCategoryById(taskCategoryId)).thenReturn(taskCategory);
        when(taskCategoryMapper.toTaskCategoryDto(taskCategory)).thenReturn(taskCategoryDTO);

        // Act
        ResponseEntity<TaskCategoryDTO> response = taskCategoryController.getTaskCategoryById(taskCategoryId);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(taskCategoryDTO, response.getBody());

        verify(taskCategoryService, times(1)).getTaskCategoryById(taskCategoryId);
        verify(taskCategoryMapper, times(1)).toTaskCategoryDto(taskCategory);
    }

    @Test
    void testUpdateTaskCategory() {
        // Arrange
        Long taskCategoryId = 1L;
        TaskCategoryDTO taskCategoryDTO = new TaskCategoryDTO(1L, "Updated Category", "Updated Description");
        TaskCategory taskCategory = new TaskCategory(1L, "Updated Category", "Updated Description");

        when(taskCategoryMapper.toTaskCategory(taskCategoryDTO)).thenReturn(taskCategory);
        when(taskCategoryService.updateTaskCategory(taskCategoryId, taskCategory)).thenReturn(taskCategory);
        when(taskCategoryMapper.toTaskCategoryDto(taskCategory)).thenReturn(taskCategoryDTO);

        // Act
        ResponseEntity<TaskCategoryDTO> response = taskCategoryController.updateTaskCategory(taskCategoryId, taskCategoryDTO);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(taskCategoryDTO, response.getBody());

        verify(taskCategoryMapper, times(1)).toTaskCategory(taskCategoryDTO);
        verify(taskCategoryService, times(1)).updateTaskCategory(taskCategoryId, taskCategory);
        verify(taskCategoryMapper, times(1)).toTaskCategoryDto(taskCategory);
    }

    @Test
    void testDeleteTaskCategory() {
        // Arrange
        Long taskCategoryId = 1L;

        doNothing().when(taskCategoryService).deleteTaskCategory(taskCategoryId);

        // Act
        ResponseEntity<Void> response = taskCategoryController.deleteTaskCategory(taskCategoryId);

        // Assert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());

        verify(taskCategoryService, times(1)).deleteTaskCategory(taskCategoryId);
    }
}
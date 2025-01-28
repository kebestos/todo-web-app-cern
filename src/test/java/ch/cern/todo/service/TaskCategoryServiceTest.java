package ch.cern.todo.service;

import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.infrastructure.repository.TaskCategoryRepository;
import ch.cern.todo.service.exception.TaskCategoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskCategoryServiceTest {

    @InjectMocks
    private TaskCategoryService taskCategoryService;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTaskCategory() {
        // Arrange
        TaskCategory taskCategory = new TaskCategory(1L, "Test Category", "Test Description");

        when(taskCategoryRepository.save(taskCategory)).thenReturn(taskCategory);

        // Act
        TaskCategory result = taskCategoryService.createTaskCategory(taskCategory);

        // Assert
        assertNotNull(result);
        assertEquals(taskCategory, result);

        verify(taskCategoryRepository, times(1)).save(taskCategory);
    }

    @Test
    void testUpdateTaskCategory() {
        // Arrange
        Long taskCategoryId = 1L;
        TaskCategory existingCategory = new TaskCategory(1L, "Old Category", "Old Description");
        TaskCategory updatedCategory = new TaskCategory(1L, "Updated Category", "Updated Description");

        when(taskCategoryRepository.findById(taskCategoryId)).thenReturn(Optional.of(existingCategory));
        when(taskCategoryRepository.save(updatedCategory)).thenReturn(updatedCategory);

        // Act
        TaskCategory result = taskCategoryService.updateTaskCategory(taskCategoryId, updatedCategory);

        // Assert
        assertNotNull(result);
        assertEquals(updatedCategory.getName(), result.getName());
        assertEquals(updatedCategory.getDescription(), result.getDescription());

        verify(taskCategoryRepository, times(1)).findById(taskCategoryId);
        verify(taskCategoryRepository, times(1)).save(updatedCategory);
    }

    @Test
    void testUpdateTaskCategory_NotFound() {
        // Arrange
        Long taskCategoryId = 1L;
        TaskCategory updatedCategory = new TaskCategory(1L, "Updated Category", "Updated Description");

        when(taskCategoryRepository.findById(taskCategoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskCategoryNotFoundException.class, () -> taskCategoryService.updateTaskCategory(taskCategoryId, updatedCategory));

        verify(taskCategoryRepository, times(1)).findById(taskCategoryId);
        verify(taskCategoryRepository, never()).save(any());
    }

    @Test
    void testGetTaskCategoryById() {
        // Arrange
        Long taskCategoryId = 1L;
        TaskCategory taskCategory = new TaskCategory(1L, "Test Category", "Test Description");

        when(taskCategoryRepository.findById(taskCategoryId)).thenReturn(Optional.of(taskCategory));

        // Act
        TaskCategory result = taskCategoryService.getTaskCategoryById(taskCategoryId);

        // Assert
        assertNotNull(result);
        assertEquals(taskCategory, result);

        verify(taskCategoryRepository, times(1)).findById(taskCategoryId);
    }

    @Test
    void testGetTaskCategoryById_NotFound() {
        // Arrange
        Long taskCategoryId = 1L;

        when(taskCategoryRepository.findById(taskCategoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TaskCategoryNotFoundException.class, () -> taskCategoryService.getTaskCategoryById(taskCategoryId));

        verify(taskCategoryRepository, times(1)).findById(taskCategoryId);
    }

    @Test
    void testDeleteTaskCategory() {
        // Arrange
        Long taskCategoryId = 1L;

        doNothing().when(taskCategoryRepository).deleteById(taskCategoryId);

        // Act
        taskCategoryService.deleteTaskCategory(taskCategoryId);

        // Assert
        verify(taskCategoryRepository, times(1)).deleteById(taskCategoryId);
    }
}
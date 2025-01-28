package ch.cern.todo.service;

import ch.cern.todo.domain.TaskQuery;
import ch.cern.todo.infrastructure.model.CustomUser;
import ch.cern.todo.infrastructure.model.Role;
import ch.cern.todo.infrastructure.model.Task;
import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.infrastructure.repository.CustomUserRepository;
import ch.cern.todo.infrastructure.repository.TaskCategoryRepository;
import ch.cern.todo.infrastructure.repository.TaskRepository;
import ch.cern.todo.service.exception.TaskCategoryNotFoundException;
import ch.cern.todo.service.exception.TaskNotFoundException;
import ch.cern.todo.service.exception.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static ch.cern.todo.service.exception.ExceptionMessage.TASK_CATEGORY_NOT_FOUND;
import static ch.cern.todo.service.exception.ExceptionMessage.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CustomUserRepository customUserRepository;

    @Mock
    private TaskCategoryRepository taskCategoryRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskService = new TaskService(taskRepository, customUserRepository, taskCategoryRepository);
    }

    @Nested
    class CreateTaskTest {
        @Test
        void createTask_ShouldCreateAndSaveTaskSuccessfully() {
            // Arrange
            Role roleAdmin = new Role(null, "ADMIN");
            Set<Role> roles_admin = new HashSet<>();
            roles_admin.add(roleAdmin);
            String username = "admin";
            CustomUser admin = new CustomUser(1L, username, "admin", roles_admin, null);
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task taskInput = new Task(null, "Task Name", "Task Description",
                    LocalDateTime.now(), taskCategory, null);
            Task taskToSave = new Task(1L, "Task Name", "Task Description",
                    taskInput.getDeadline(), taskCategory, admin);

            when(customUserRepository.findByUsername(username)).thenReturn(Optional.of(admin));
            when(taskCategoryRepository.findById(taskCategory.getId())).thenReturn(Optional.of(taskCategory));
            when(taskRepository.save(any(Task.class))).thenReturn(taskToSave);

            // Act
            Task savedTask = taskService.createTask(taskInput, username);

            // Assert
            assertNotNull(savedTask);
            assertEquals(taskToSave.getId(), savedTask.getId());
            assertEquals(taskToSave.getName(), savedTask.getName());
            assertEquals(taskToSave.getDescription(), savedTask.getDescription());
            assertEquals(taskToSave.getDeadline(), savedTask.getDeadline());
            assertEquals(taskToSave.getCategory(), savedTask.getCategory());
            assertEquals(taskToSave.getUser(), savedTask.getUser());

            verify(customUserRepository, times(1)).findByUsername(username);
            verify(taskCategoryRepository, times(1)).findById(taskCategory.getId());
            verify(taskRepository, times(1)).save(any(Task.class));
        }

        @Test
        void createTask_ShouldThrowUsernameNotFoundException_WhenUserNotFound() {
            // Arrange
            String username = "nonExistentUser";
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task taskInput = new Task(null, "Task Name", "Task Description", LocalDateTime.now(), taskCategory, null);

            when(customUserRepository.findByUsername(username)).thenReturn(Optional.empty());

            // Act & Assert
            UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                    taskService.createTask(taskInput, username)
            );

            assertEquals(USER_NOT_FOUND.getMessage(), exception.getMessage());
            verify(customUserRepository, times(1)).findByUsername(username);
            verify(taskCategoryRepository, never()).findById(anyLong());
            verify(taskRepository, never()).save(any(Task.class));
        }

        @Test
        void createTask_ShouldThrowTaskCategoryNotFoundException_WhenTaskCategoryNotFound() {
            // Arrange
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            String username = "user";
            CustomUser user = new CustomUser(1L, username, "user", roles_user, null);
            TaskCategory taskCategory = new TaskCategory(4L, "nonExistentTaskCategory", "nonExistentTaskCategory");
            Task taskInput = new Task(null, "Task Name", "Task Description", LocalDateTime.now(), taskCategory, null);

            when(customUserRepository.findByUsername(username)).thenReturn(Optional.of(user));
            when(taskCategoryRepository.findById(taskCategory.getId())).thenReturn(Optional.empty());

            // Act & Assert
            TaskCategoryNotFoundException exception = assertThrows(TaskCategoryNotFoundException.class, () ->
                    taskService.createTask(taskInput, username)
            );

            // Assert
            assertEquals(TASK_CATEGORY_NOT_FOUND.getMessage(), exception.getMessage());
            verify(customUserRepository, times(1)).findByUsername(username);
            verify(taskCategoryRepository, times(1)).findById(taskCategory.getId());
            verify(taskRepository, never()).save(any(Task.class));
        }
    }

    @Nested
    class UpdateTaskTest {
        @Test
        void updateTask_shouldUpdateTaskSuccessfully() {
            // Arrange
            Long taskId = 1L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            String username = "user";
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task existingTask = new Task(1L, "Task Name 1", "Task Description",
                    LocalDateTime.now(), taskCategory, null);
            List<Task> tasks = new ArrayList<>();
            tasks.add(existingTask);
            CustomUser user = new CustomUser(1L, username, "user", roles_user, tasks);

            Task taskToUpdate = new Task(1L, "Task Name 2", "Task Description",
                    LocalDateTime.now(), taskCategory, null);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskCategoryRepository.findById(taskToUpdate.getCategory().getId())).thenReturn(Optional.of(taskCategory));
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
            when(taskRepository.save(any())).thenReturn(existingTask);

            // Act
            taskService.updateTask(taskId, taskToUpdate, userName);

            // Assert
            verify(taskRepository).save(any(Task.class));
        }

        @Test
        void updateTask_shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            Long taskId = 1L;
            String userName = "nonExistentUser";
            Task taskToUpdate = new Task();

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> taskService.updateTask(taskId, taskToUpdate, userName));
        }

        @Test
        void updateTask_shouldThrowExceptionWhenTaskCategoryNotFound() {
            // Arrange
            Long taskId = 1L;
            String userName = "user";
            TaskCategory taskCategory = new TaskCategory(99L, "NonExistentCategory", "NonExistentCategory");
            Task taskToUpdate = new Task(null, "Task Name 2", "Task Description",
                    LocalDateTime.now(), taskCategory, null);

            CustomUser user = new CustomUser();

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskCategoryRepository.findById(taskToUpdate.getCategory().getId())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TaskCategoryNotFoundException.class, () -> taskService.updateTask(taskId, taskToUpdate, userName));
        }

        @Test
        void updateTask_shouldThrowExceptionWhenTaskNotFound() {
            // Arrange
            Long taskId = 999L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            String username = "user";
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task existingTask = new Task(1L, "Task Name 1", "Task Description",
                    LocalDateTime.now(), taskCategory, null);
            List<Task> tasks = new ArrayList<>();
            tasks.add(existingTask);
            CustomUser user = new CustomUser(1L, username, "user", roles_user, tasks);

            Task taskToUpdate = new Task(taskId, "NonExistentTask", "NonExistentTask",
                    LocalDateTime.now(), taskCategory, null);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskCategoryRepository.findById(taskToUpdate.getCategory().getId())).thenReturn(Optional.of(new TaskCategory()));
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskId, taskToUpdate, userName));
        }

        @Test
        void updateTask_shouldThrowExceptionWhenUnauthorized() {
            // Arrange
            Long taskId = 64L;
            String userName = "user";

            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            String username = "user";
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task taskOfTheUser = new Task(1L, "Task belong to user", "Task Description", LocalDateTime.now(), taskCategory, null);

            List<Task> tasks = new ArrayList<>();
            tasks.add(taskOfTheUser);

            CustomUser user = new CustomUser(1L, username, "user", roles_user, tasks);
            Task taskToUpdate = new Task(64L, "Task doesn't belong to user", "Task Description", LocalDateTime.now(), taskCategory, user);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskCategoryRepository.findById(taskToUpdate.getCategory().getId())).thenReturn(Optional.of(taskCategory));
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskToUpdate));

            // Act & Assert
            assertThrows(UnAuthorizedException.class, () -> taskService.updateTask(taskId, taskToUpdate, userName));
        }
    }

    @Nested
    class GetTaskByIdTest {

        @Test
        void getTaskById_shouldReturnTaskSuccessfully() {
            // Arrange
            Long taskId = 1L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task existingTask = new Task(1L, "Task Name 1", "Task Description",
                    LocalDateTime.now(), taskCategory, null);
            List<Task> tasks = new ArrayList<>();
            tasks.add(existingTask);
            CustomUser user = new CustomUser(1L, userName, "user", roles_user, tasks);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

            // Act
            Task result = taskService.getTaskById(taskId, userName);

            // Assert
            assertEquals(existingTask, result);
            verify(taskRepository).findById(taskId);
        }

        @Test
        void getTaskById_shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            Long taskId = 1L;
            String userName = "nonExistentUser";

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> taskService.getTaskById(taskId, userName));
        }

        @Test
        void getTaskById_shouldThrowExceptionWhenTaskNotFound() {
            // Arrange
            Long taskId = 1L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task existingTask = new Task(1L, "nonExistentTask", "nonExistentTask",
                    LocalDateTime.now(), taskCategory, null);
            List<Task> tasks = new ArrayList<>();
            tasks.add(existingTask);
            CustomUser user = new CustomUser(1L, userName, "user", roles_user, tasks);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId, userName));
        }

        @Test
        void getTaskById_shouldThrowExceptionWhenUnauthorized() {
            // Arrange
            Long taskId = 999L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task existingTask = new Task(1L, "Task Name 1", "Task Description",
                    LocalDateTime.now(), taskCategory, null);
            List<Task> tasks = new ArrayList<>();
            tasks.add(existingTask);
            CustomUser user = new CustomUser(1L, userName, "user", roles_user, tasks);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));

            // Act & Assert
            assertThrows(UnAuthorizedException.class, () -> taskService.getTaskById(taskId, userName));
        }
    }

    @Nested
    class DeleteTaskTest {
        @Test
        void deleteTask_shouldDeleteTaskSuccessfully() {
            // Arrange
            Long taskId = 1L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task existingTask = new Task(1L, "Task Name 1", "Task Description",
                    LocalDateTime.now(), taskCategory, null);
            List<Task> tasks = new ArrayList<>();
            tasks.add(existingTask);
            CustomUser user = new CustomUser(1L, userName, "user", roles_user, tasks);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

            // Act
            taskService.deleteTask(taskId, userName);

            // Assert
            verify(taskRepository).deleteById(taskId);
        }

        @Test
        void deleteTask_shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            Long taskId = 1L;
            String userName = "nonExistentUser";

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> taskService.deleteTask(taskId, userName));
        }

        @Test
        void deleteTask_shouldThrowExceptionWhenTaskNotFound() {
            // Arrange
            Long taskId = 1L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task existingTask = new Task(1L, "nonExistentTask", "nonExistentTask",
                    LocalDateTime.now(), taskCategory, null);
            List<Task> tasks = new ArrayList<>();
            tasks.add(existingTask);
            CustomUser user = new CustomUser(1L, userName, "user", roles_user, tasks);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(taskId, userName));
        }

        @Test
        void deleteTask_shouldThrowExceptionWhenUnauthorized() {
            // Arrange
            Long taskId = 999L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task existingTask = new Task(1L, "Task Name 1", "Task Description",
                    LocalDateTime.now(), taskCategory, null);
            List<Task> tasks = new ArrayList<>();
            tasks.add(existingTask);
            CustomUser user = new CustomUser(1L, userName, "user", roles_user, tasks);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

            // Act & Assert
            assertThrows(UnAuthorizedException.class, () -> taskService.deleteTask(taskId, userName));
        }
    }

    @Nested
    class getTasksByQueryTest {

        @Test
        void getTasksByQuery_shouldReturnTasksSuccessfully() {
            // Arrange
            TaskQuery taskQuery = new TaskQuery("Task", "A Task", null, 1L, 1L, "GREATER");

            Long taskId = 1L;
            String userName = "user";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            TaskCategory taskCategory = new TaskCategory(1L, "Dev", "Technical development task");
            Task task = new Task(1L, "Test Task", "Task Description",
                    LocalDateTime.now(), taskCategory, null);
            Task task2 = new Task(2L, "Test Task 2", "Task Description 2",
                    LocalDateTime.now(), taskCategory, null);

            List<Task> tasksMock = new ArrayList<>();
            tasksMock.add(task);
            tasksMock.add(task2);
            CustomUser user = new CustomUser(1L, userName, "user", roles_user, tasksMock);

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.of(user));
            when(taskRepository.findAll(any(Specification.class))).thenReturn(tasksMock);

            // Act
            List<Task> tasks = taskService.getTasksByQuery(taskQuery, userName);

            // Assert
            assertNotNull(tasks);
            assertEquals(2, tasks.size());
            assertEquals("Test Task", tasks.get(0).getName());
            assertEquals("Test Task 2", tasks.get(1).getName());

            // Verify interactions
            verify(customUserRepository, times(1)).findByUsername(userName);
            verify(taskRepository, times(1)).findAll(any(Specification.class));
        }

        @Test
        void getTasksByQuery_shouldThrowExceptionWhenUserNotFound() {
            // Arrange
            TaskQuery taskQuery = new TaskQuery("Task", "A Task", null, 1L, 1L, "GREATER");
            String userName = "nonExistentUser";

            when(customUserRepository.findByUsername(userName)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(UsernameNotFoundException.class, () -> taskService.getTasksByQuery(taskQuery, userName));

            // Verify interactions
            verify(customUserRepository, times(1)).findByUsername(userName);
            verify(taskRepository, never()).findAll(any(Specification.class));
        }

        @Test
        void getTasksByQuery_shouldThrowExceptionWhenUnauthorized() {
            // Arrange
            String username = "unauthorizedUser";
            Role roleUser = new Role(null, "USER");
            Set<Role> roles_user = new HashSet<>();
            roles_user.add(roleUser);
            TaskQuery taskQuery = new TaskQuery("Task", "A Task", null, 1L, 2L, "GREATER");

            CustomUser user = new CustomUser(1L, username, "user", roles_user, null);

            when(customUserRepository.findByUsername(username)).thenReturn(Optional.of(user));

            // Act & Assert
            assertThrows(UnAuthorizedException.class, () -> taskService.getTasksByQuery(taskQuery, username));

            // Verify interactions
            verify(customUserRepository, times(1)).findByUsername(username);
            verify(taskRepository, never()).findAll(any(Specification.class));
        }
    }
}
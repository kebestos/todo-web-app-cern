package ch.cern.todo.controller;

import ch.cern.todo.controller.dto.TaskCategoryDTO;
import ch.cern.todo.controller.dto.TaskDTO;
import ch.cern.todo.controller.dto.TaskQueryDTO;
import ch.cern.todo.controller.dto.mapper.TaskMapper;
import ch.cern.todo.controller.dto.mapper.TaskQueryMapper;
import ch.cern.todo.domain.TaskQuery;
import ch.cern.todo.infrastructure.model.CustomUser;
import ch.cern.todo.infrastructure.model.Role;
import ch.cern.todo.infrastructure.model.Task;
import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.service.TaskService;
import ch.cern.todo.service.exception.TaskCategoryNotFoundException;
import ch.cern.todo.service.exception.TaskNotFoundException;
import ch.cern.todo.service.exception.UnAuthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ch.cern.todo.service.exception.ExceptionMessage.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockitoBean
    private TaskService taskService;

    @MockitoBean
    private TaskMapper taskMapper;

    @MockitoBean
    private TaskQueryMapper taskQueryMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void createTask() throws Exception {
        Set<Role> roles_admin = new HashSet<>();
        roles_admin.add(new Role(null, "ADMIN"));
        Task task = new Task(1L, "API Post task", "make an api rest to create task",
                LocalDateTime.of(2013, 4, 23, 18, 30, 20),
                new TaskCategory(1L, "Dev", "Technical development task"),
                new CustomUser(null, "admin", "admin", roles_admin, null));

        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        Mockito.when(taskMapper.toTask(Mockito.any(TaskDTO.class))).thenReturn(task);
        Mockito.when(taskService.createTask(Mockito.any(Task.class), Mockito.any(String.class))).thenReturn(task);
        Mockito.when(taskMapper.toTaskDto(Mockito.any(Task.class))).thenReturn(taskDto);

        User user = new User("admin", "admin", AuthorityUtils.createAuthorityList("ADMIN"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .principal(testingAuthenticationToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("API Post task")))
                .andExpect(jsonPath("$.description", is("make an api rest to create task")))
                .andExpect(jsonPath("$.deadline", is("2013-04-23T18:25:43")))
                .andExpect(jsonPath("$.taskCategory.id", is(1)))
                .andExpect(jsonPath("$.taskCategory.name", is("Dev")))
                .andExpect(jsonPath("$.taskCategory.description", is("Technical development task")));
    }

    @Test
    void getTaskById() throws Exception {

        Set<Role> roles_admin = new HashSet<>();
        roles_admin.add(new Role(null, "ADMIN"));
        Task task = new Task(1L, "API Post task", "make an api rest to create task",
                LocalDateTime.of(2013, 4, 23, 18, 30, 20),
                new TaskCategory(1L, "Dev", "Technical development task"),
                new CustomUser(null, "admin", "admin", roles_admin, null));

        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        Mockito.when(taskService.getTaskById(1L, "admin")).thenReturn(task);
        Mockito.when(taskMapper.toTaskDto(Mockito.any(Task.class))).thenReturn(taskDto);

        User user = new User("admin", "admin", AuthorityUtils.createAuthorityList("ADMIN"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(get("/api/task/1")
                        .principal(testingAuthenticationToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("API Post task")))
                .andExpect(jsonPath("$.description", is("make an api rest to create task")))
                .andExpect(jsonPath("$.deadline", is("2013-04-23T18:25:43")))
                .andExpect(jsonPath("$.taskCategory.id", is(1)))
                .andExpect(jsonPath("$.taskCategory.name", is("Dev")))
                .andExpect(jsonPath("$.taskCategory.description", is("Technical development task")));
    }

    @Test
    void updateTask() throws Exception {

        Set<Role> roles_admin = new HashSet<>();
        roles_admin.add(new Role(null, "ADMIN"));
        Task task = new Task(1L, "API Post task", "make an api rest to create task",
                LocalDateTime.of(2013, 4, 23, 18, 30, 20),
                new TaskCategory(1L, "Dev", "Technical development task"),
                new CustomUser(null, "admin", "admin", roles_admin, null));

        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        Mockito.when(taskMapper.toTask(Mockito.any(TaskDTO.class))).thenReturn(task);
        Mockito.when(taskService.updateTask(Mockito.any(Long.class), Mockito.any(Task.class), Mockito.any(String.class))).thenReturn(task);
        Mockito.when(taskMapper.toTaskDto(Mockito.any(Task.class))).thenReturn(taskDto);

        User user = new User("admin", "admin", AuthorityUtils.createAuthorityList("ADMIN"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(put("/api/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .principal(testingAuthenticationToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("API Post task")))
                .andExpect(jsonPath("$.description", is("make an api rest to create task")))
                .andExpect(jsonPath("$.deadline", is("2013-04-23T18:25:43")))
                .andExpect(jsonPath("$.taskCategory.id", is(1)))
                .andExpect(jsonPath("$.taskCategory.name", is("Dev")))
                .andExpect(jsonPath("$.taskCategory.description", is("Technical development task")));
    }

    @Test
    void updateTaskUnAuthorizedException() throws Exception {

        Set<Role> roles_user = new HashSet<>();
        roles_user.add(new Role(null, "USER"));
        Task task = new Task(1L, "API Post task", "make an api rest to create task",
                LocalDateTime.of(2013, 4, 23, 18, 30, 20),
                new TaskCategory(1L, "Dev", "Technical development task"),
                new CustomUser(null, "user", "user", roles_user, null));

        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        UnAuthorizedException unAuthorizedException = new UnAuthorizedException(UNAUTHORIZED_ACCESS.getMessage());

        Mockito.when(taskMapper.toTask(Mockito.any(TaskDTO.class))).thenReturn(task);
        Mockito.when(taskService.updateTask(Mockito.any(Long.class), Mockito.any(Task.class), Mockito.any(String.class))).thenThrow(unAuthorizedException);

        User user = new User("user", "user", AuthorityUtils.createAuthorityList("USER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(put("/api/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .principal(testingAuthenticationToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status", is(401)))
                .andExpect(jsonPath("$.error", is("Unauthorized")))
                .andExpect(jsonPath("$.message", is(UNAUTHORIZED_ACCESS.getMessage())))
                .andExpect(jsonPath("$.path", is("uri=/api/task/1")));
    }

    @Test
    void updateTaskNotFoundException() throws Exception {

        Set<Role> roles_user = new HashSet<>();
        roles_user.add(new Role(null, "USER"));
        Task task = new Task(1L, "API Post task", "make an api rest to create task",
                LocalDateTime.of(2013, 4, 23, 18, 30, 20),
                new TaskCategory(1L, "Dev", "Technical development task"),
                new CustomUser(null, "user", "user", roles_user, null));

        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        TaskNotFoundException taskNotFoundException = new TaskNotFoundException(TASK_NOT_FOUND.getMessage());

        Mockito.when(taskMapper.toTask(Mockito.any(TaskDTO.class))).thenReturn(task);
        Mockito.when(taskService.updateTask(Mockito.any(Long.class), Mockito.any(Task.class), Mockito.any(String.class))).thenThrow(taskNotFoundException);

        User user = new User("user", "user", AuthorityUtils.createAuthorityList("USER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(put("/api/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .principal(testingAuthenticationToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is(TASK_NOT_FOUND.getMessage())))
                .andExpect(jsonPath("$.path", is("uri=/api/task/1")));
    }

    @Test
    void updateTaskCategoryNotFoundException() throws Exception {

        Set<Role> roles_user = new HashSet<>();
        roles_user.add(new Role(null, "USER"));
        Task task = new Task(1L, "API Post task", "make an api rest to create task",
                LocalDateTime.of(2013, 4, 23, 18, 30, 20),
                new TaskCategory(1L, "Dev", "Technical development task"),
                new CustomUser(null, "user", "user", roles_user, null));

        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        TaskCategoryNotFoundException taskCategoryNotFoundException = new TaskCategoryNotFoundException(TASK_CATEGORY_NOT_FOUND.getMessage());

        Mockito.when(taskMapper.toTask(Mockito.any(TaskDTO.class))).thenReturn(task);
        Mockito.when(taskService.updateTask(Mockito.any(Long.class), Mockito.any(Task.class), Mockito.any(String.class))).thenThrow(taskCategoryNotFoundException);

        User user = new User("user", "user", AuthorityUtils.createAuthorityList("USER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(put("/api/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .principal(testingAuthenticationToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is(TASK_CATEGORY_NOT_FOUND.getMessage())))
                .andExpect(jsonPath("$.path", is("uri=/api/task/1")));
    }

    @Test
    void updateUsernameNotFoundException() throws Exception {

        Set<Role> roles_user = new HashSet<>();
        roles_user.add(new Role(null, "USER"));
        Task task = new Task(1L, "API Post task", "make an api rest to create task",
                LocalDateTime.of(2013, 4, 23, 18, 30, 20),
                new TaskCategory(1L, "Dev", "Technical development task"),
                new CustomUser(null, "user", "user", roles_user, null));

        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        UsernameNotFoundException usernameNotFoundException = new UsernameNotFoundException(USER_NOT_FOUND.getMessage());

        Mockito.when(taskMapper.toTask(Mockito.any(TaskDTO.class))).thenReturn(task);
        Mockito.when(taskService.updateTask(Mockito.any(Long.class), Mockito.any(Task.class), Mockito.any(String.class))).thenThrow(usernameNotFoundException);

        User user = new User("user", "user", AuthorityUtils.createAuthorityList("USER"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(put("/api/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .principal(testingAuthenticationToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is(USER_NOT_FOUND.getMessage())))
                .andExpect(jsonPath("$.path", is("uri=/api/task/1")));
    }

    @Test
    void deleteTask() throws Exception {

        Mockito.doNothing().when(taskService).deleteTask(1L, "admin");

        User user = new User("admin", "admin", AuthorityUtils.createAuthorityList("ADMIN"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        mockMvc.perform(delete("/api/task/1")
                        .principal(testingAuthenticationToken))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTasksByQuery() throws Exception {

        Set<Role> roles_admin = new HashSet<>();
        roles_admin.add(new Role(null, "ADMIN"));
        Task task = new Task(1L, "API Post task", "make an api rest to create task",
                LocalDateTime.of(2013, 4, 23, 18, 30, 20),
                new TaskCategory(1L, "Dev", "Technical development task"),
                new CustomUser(null, "admin", "admin", roles_admin, null));

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);

        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        List<TaskDTO> taskDTOS = new ArrayList<>();
        taskDTOS.add(taskDto);

        User user = new User("admin", "admin", AuthorityUtils.createAuthorityList("ADMIN"));
        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(user, null);

        TaskQueryDTO taskQueryDTO = new TaskQueryDTO("API Post task", "make an api rest to create",
                "2013-04-23T18:25:43", 1L, 1L, "EQUAL");

        TaskQuery taskQuery = new TaskQuery("API Post task", "make an api rest to create",
                LocalDateTime.of(2013, 4, 23, 18, 25, 43), 1L, 1L,"EQUAL");

        Mockito.when(taskQueryMapper.toTaskQuery(Mockito.any(TaskQueryDTO.class))).thenReturn(taskQuery);
        Mockito.when(taskService.getTasksByQuery(Mockito.any(TaskQuery.class), Mockito.any(String.class))).thenReturn(tasks);
        Mockito.when(taskMapper.toTaskDtoList(Mockito.anyList())).thenReturn(taskDTOS);

        mockMvc.perform(get("/api/task/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskQueryDTO))
                        .principal(testingAuthenticationToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("API Post task")))
                .andExpect(jsonPath("$[0].description", is("make an api rest to create task")))
                .andExpect(jsonPath("$[0].deadline", is("2013-04-23T18:25:43")))
                .andExpect(jsonPath("$[0].taskCategory.id", is(1)))
                .andExpect(jsonPath("$[0].taskCategory.name", is("Dev")))
                .andExpect(jsonPath("$[0].taskCategory.description", is("Technical development task")));
    }
}
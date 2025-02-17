package ch.cern.todo.integration;

import ch.cern.todo.controller.dto.TaskCategoryDTO;
import ch.cern.todo.controller.dto.TaskDTO;
import ch.cern.todo.controller.dto.mapper.TaskMapper;
import ch.cern.todo.controller.dto.mapper.TaskQueryMapper;
import ch.cern.todo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static ch.cern.todo.service.exception.ExceptionMessage.USER_NOT_FOUND;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private TaskQueryMapper taskQueryMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Simulate an authenticated user
    public void createTask() throws Exception {
        // Arrange
        TaskDTO taskDto = new TaskDTO(null, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        // Act & Assert
        mockMvc.perform(post("/api/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("API Post task")))
                .andExpect(jsonPath("$.description", is("make an api rest to create task")))
                .andExpect(jsonPath("$.deadline", is("2013-04-23T18:25:43")))
                .andExpect(jsonPath("$.taskCategory.id", is(1)))
                .andExpect(jsonPath("$.taskCategory.name", is("Dev")))
                .andExpect(jsonPath("$.taskCategory.description", is("Technical development task")));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Simulate an authenticated user
    public void getTaskById() throws Exception {
        // Arrange
        TaskDTO taskDto = new TaskDTO(null, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        // Act & Assert
        mockMvc.perform(get("/api/task/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("API Post task")))
                .andExpect(jsonPath("$.description", is("make an api rest to create task")))
                .andExpect(jsonPath("$.deadline", is("2013-04-23T18:30:20")))
                .andExpect(jsonPath("$.taskCategory.id", is(1)))
                .andExpect(jsonPath("$.taskCategory.name", is("Dev")))
                .andExpect(jsonPath("$.taskCategory.description", is("Technical development task")));
    }

    @Test
    @WithMockUser(username = "nonexistent_user", roles = {"USER"}) // Simulate an authenticated user
    public void updateUsernameNotFoundException() throws Exception {
        // Arrange
        TaskDTO taskDto = new TaskDTO(1L, "API Post task", "make an api rest to create task", "2013-04-23T18:25:43",
                new TaskCategoryDTO(1L, "Dev", "Technical development task"));

        // Act & Assert
        mockMvc.perform(put("/api/task/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.error", is("Not Found")))
                .andExpect(jsonPath("$.message", is(USER_NOT_FOUND.getMessage())))
                .andExpect(jsonPath("$.path", is("uri=/api/task/1")));
    }
}

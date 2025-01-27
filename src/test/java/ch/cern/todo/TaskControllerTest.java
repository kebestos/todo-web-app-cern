package ch.cern.todo;

import ch.cern.todo.controller.TaskController;
import ch.cern.todo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;

//@SpringBootTest
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;


//    @Test
//    void testGetTaskById() throws Exception {
//        Task task = new Task(1L,"Task 1", "make an algorithm", Date.valueOf("2024-12-25"),1L);
//
//        Mockito.when(taskService.getTaskById(anyLong())).thenReturn(task);
//
//        mockMvc.perform(get("/api/task/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.name").value("Task 1"))
//                .andExpect(jsonPath("$.description").value("make an algorithm"))
//                .andExpect(jsonPath("$.deadline").value(Date.valueOf("2024-12-25")))
//                .andExpect(jsonPath("$.categoryId").value(1L));
//    }

}

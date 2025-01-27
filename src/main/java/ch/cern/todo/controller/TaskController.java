package ch.cern.todo.controller;

import ch.cern.todo.controller.dto.TaskDTO;
import ch.cern.todo.controller.dto.mapper.TaskMapper;
import ch.cern.todo.domain.TaskQuery;
import ch.cern.todo.infrastructure.model.Task;
import ch.cern.todo.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    //TODO logger & explain autowired

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto, Principal principal) {

        String userName = principal.getName();

        Task task = taskMapper.toTask(taskDto);

        Task taskCreated = taskService.createTask(task, userName);

        TaskDTO taskCreatedDto = taskMapper.toTaskDto(taskCreated);

        return ResponseEntity.ok(taskCreatedDto);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId, Principal principal) {

        String userName = principal.getName();

        Task task = taskService.getTaskById(taskId, userName);

        TaskDTO taskDto = taskMapper.toTaskDto(task);

        return ResponseEntity.ok(taskDto);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId, @RequestBody TaskDTO taskDto, Principal principal) {

        String userName = principal.getName();

        Task taskToUpdate = taskMapper.toTask(taskDto);

        Task taskUpdated = taskService.updateTask(taskId, taskToUpdate, userName);

        TaskDTO taskUpdatedDto = taskMapper.toTaskDto(taskUpdated);

        return ResponseEntity.ok(taskUpdatedDto);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, Principal principal) {

        String userName = principal.getName();

        taskService.deleteTask(taskId, userName);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> getTasksByQuery(@RequestBody TaskQuery taskQuery, Principal principal) {

        String userName = principal.getName();

        List<Task> tasks = taskService.getTasksByQuery(taskQuery, userName);

        List<TaskDTO> taskDTOs = taskMapper.toTaskDtoList(tasks);

        return ResponseEntity.ok(taskDTOs);
    }
}

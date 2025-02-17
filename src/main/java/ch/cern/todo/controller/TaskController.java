package ch.cern.todo.controller;

import ch.cern.todo.controller.dto.TaskDTO;
import ch.cern.todo.controller.dto.TaskQueryDTO;
import ch.cern.todo.controller.dto.mapper.TaskMapper;
import ch.cern.todo.controller.dto.mapper.TaskQueryMapper;
import ch.cern.todo.domain.TaskQuery;
import ch.cern.todo.infrastructure.model.Task;
import ch.cern.todo.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    private final TaskQueryMapper taskQueryMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    public TaskController(TaskService taskService, TaskMapper taskMapper, TaskQueryMapper taskQueryMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.taskQueryMapper = taskQueryMapper;
    }

    /**
     * EndPoint REST to create new Task
     * @param taskDto - Request Body parameter with information to create a new task
     * @param principal - Principal to get all data from the current user in the system
     * @return TaskDTO is return if the service successfully saved a new Task
     */
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto, Principal principal) {
        LOGGER.info("createTask is called in TaskController with parameter TaskDTO: {} and Principal {}", taskDto, principal);

        String userName = principal.getName();
        LOGGER.info("Principal has userName: {}", userName);

        Task task = taskMapper.toTask(taskDto);
        LOGGER.info("mapper toTask is called in createTask with output Task: {}", task);

        Task taskCreated = taskService.createTask(task, userName);
        LOGGER.info("service createTask is called with output Task created: {}", taskCreated);

        TaskDTO taskCreatedDto = taskMapper.toTaskDto(taskCreated);
        LOGGER.info("mapper toTaskDto is called in createTask with output TaskDTO: {}", taskCreatedDto);

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
    public ResponseEntity<List<TaskDTO>> getTasksByQuery(@RequestBody TaskQueryDTO taskQueryDto, Principal principal) {

        String userName = principal.getName();

        TaskQuery taskQuery = taskQueryMapper.toTaskQuery(taskQueryDto);

        List<Task> tasks = taskService.getTasksByQuery(taskQuery, userName);

        List<TaskDTO> taskDTOs = taskMapper.toTaskDtoList(tasks);

        return ResponseEntity.ok(taskDTOs);
    }
}

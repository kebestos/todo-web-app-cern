package ch.cern.todo.controller;

import ch.cern.todo.controller.dto.TaskDTO;
import ch.cern.todo.controller.dto.TaskMapper;
import ch.cern.todo.infrastructure.entity.Task;
import ch.cern.todo.model.TaskQuery;
import ch.cern.todo.service.task.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    //TODO logger & explain autowired

    private final TaskService taskService;

//    @Autowired
//    private ModelMapper modelMapper;

    private final TaskMapper taskMapper;

    //    @Autowired
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDto, Principal principal) {
        try {
            String userName = principal.getName();

            Task task = taskMapper.toTask(taskDto);

            Task taskCreated = taskService.createTask(task, userName);

            TaskDTO taskCreatedDto = taskMapper.toTaskDto(taskCreated);

            return ResponseEntity.ok(taskCreatedDto);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId, Principal principal) {
        try {
            String userName = principal.getName();

            Task task = taskService.getTaskById(taskId, userName);

            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody Task task, Principal principal) {
        try {
            String userName = principal.getName();

            Task taskUpdated = taskService.updateTask(taskId, task, userName);

            return ResponseEntity.ok(taskUpdated);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, Principal principal) {
        try {
            String userName = principal.getName();

            taskService.deleteTask(taskId, userName);

            //TODO return content ?
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Task>> getTasksByQuery(@RequestBody TaskQuery taskQuery, Principal principal) {
        try {
            String userName = principal.getName();

            List<Task> tasks = taskService.getTasksByQuery(taskQuery, userName);

            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    private TaskDTO convertToDto(Task task, TimeZone timeZone) {
//        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
//        taskDTO.setDeadlineDate(task.getDeadline(), timeZone.toString());
//        return taskDTO;
//    }
//
//    private Task convertToEntity(TaskDTO taskDTO) throws ParseException {
//        Task task = modelMapper.map(taskDTO, Task.class);
//
//        return task;
//    }
}

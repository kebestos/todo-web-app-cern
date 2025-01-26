package ch.cern.todo.controller;

import ch.cern.todo.infrastructure.entity.Task;
import ch.cern.todo.model.TaskQuery;
import ch.cern.todo.service.task.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task, Principal principal) {
        try {
            String userName = principal.getName();

            Task taskCreated = taskService.createTask(task, userName);

            return ResponseEntity.ok(taskCreated);
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
}

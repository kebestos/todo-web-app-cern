package ch.cern.todo.controller;

import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.service.TaskCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task/category")
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    public TaskCategoryController(TaskCategoryService taskCategoryService) {
        this.taskCategoryService = taskCategoryService;
    }

    @PostMapping
    public ResponseEntity<TaskCategory> createTaskCategory(@RequestBody TaskCategory taskCategory) {
        try {
            return ResponseEntity.ok(taskCategoryService.createTaskCategory(taskCategory));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{taskCategoryId}")
    public ResponseEntity<TaskCategory> getTaskCategoryById(@PathVariable Long taskCategoryId) {
        try {
            return ResponseEntity.ok(taskCategoryService.getTaskCategoryById(taskCategoryId));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{taskCategoryId}")
    public ResponseEntity<TaskCategory> updateTaskCategory(@PathVariable Long taskCategoryId, @RequestBody TaskCategory taskCategory) {
        try {
            return ResponseEntity.ok(taskCategoryService.updateTaskCategory(taskCategoryId, taskCategory));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{taskCategoryId}")
    public ResponseEntity<Void> deleteTaskCategory(@PathVariable Long taskCategoryId) {
        try {
            taskCategoryService.deleteTaskCategory(taskCategoryId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

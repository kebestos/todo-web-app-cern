package ch.cern.todo.controller;

import ch.cern.todo.controller.dto.TaskCategoryDTO;
import ch.cern.todo.controller.dto.mapper.TaskCategoryMapper;
import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.service.TaskCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task/category")
public class TaskCategoryController {

    private final TaskCategoryService taskCategoryService;

    private final TaskCategoryMapper taskCategoryMapper;

    public TaskCategoryController(TaskCategoryService taskCategoryService, TaskCategoryMapper taskCategoryMapper) {
        this.taskCategoryService = taskCategoryService;
        this.taskCategoryMapper = taskCategoryMapper;
    }

    @PostMapping
    public ResponseEntity<TaskCategoryDTO> createTaskCategory(@RequestBody TaskCategoryDTO taskCategoryDto) {
        try {
            TaskCategory taskCategory = taskCategoryMapper.toTaskCategory(taskCategoryDto);

            TaskCategory taskCategoryCreated = taskCategoryService.createTaskCategory(taskCategory);

            TaskCategoryDTO taskCategoryCreatedDTO = taskCategoryMapper.toTaskCategoryDto(taskCategoryCreated);

            return ResponseEntity.ok(taskCategoryCreatedDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{taskCategoryId}")
    public ResponseEntity<TaskCategoryDTO> getTaskCategoryById(@PathVariable Long taskCategoryId) {
        try {
            TaskCategory taskCategory = taskCategoryService.getTaskCategoryById(taskCategoryId);

            TaskCategoryDTO taskCategoryDTO = taskCategoryMapper.toTaskCategoryDto(taskCategory);

            return ResponseEntity.ok(taskCategoryDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{taskCategoryId}")
    public ResponseEntity<TaskCategoryDTO> updateTaskCategory(@PathVariable Long taskCategoryId, @RequestBody TaskCategoryDTO taskCategoryDTO) {
        try {
            TaskCategory taskCategory = taskCategoryMapper.toTaskCategory(taskCategoryDTO);

            TaskCategory taskCategoryUpdated = taskCategoryService.updateTaskCategory(taskCategoryId, taskCategory);

            TaskCategoryDTO taskCategoryCreatedDTO = taskCategoryMapper.toTaskCategoryDto(taskCategoryUpdated);

            return ResponseEntity.ok(taskCategoryCreatedDTO);
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

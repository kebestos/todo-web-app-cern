package ch.cern.todo.service;

import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.infrastructure.repository.TaskCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskCategoryService {

    private final TaskCategoryRepository taskCategoryRepository;


    public TaskCategoryService(TaskCategoryRepository taskCategoryRepository) {
        this.taskCategoryRepository = taskCategoryRepository;
    }

    public TaskCategory createTaskCategory(TaskCategory taskCategory) {
        return taskCategoryRepository.save(taskCategory);
    }

    public TaskCategory updateTaskCategory(Long taskCategoryId, TaskCategory taskCategoryUpdate) {
        TaskCategory taskCategoryUpdated = new TaskCategory(taskCategoryId, taskCategoryUpdate.getName(), taskCategoryUpdate.getDescription());
        return taskCategoryRepository.save(taskCategoryUpdated);
    }

    public TaskCategory getTaskCategoryById(Long taskCategoryId) {
        return taskCategoryRepository.findById(taskCategoryId).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTaskCategory(Long taskCategoryId) {
        taskCategoryRepository.deleteById(taskCategoryId);
    }
}

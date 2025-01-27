package ch.cern.todo.service;

import ch.cern.todo.domain.TaskQuery;
import ch.cern.todo.infrastructure.model.CustomUser;
import ch.cern.todo.infrastructure.model.Task;
import ch.cern.todo.infrastructure.model.TaskCategory;
import ch.cern.todo.infrastructure.repository.CustomUserRepository;
import ch.cern.todo.infrastructure.repository.TaskCategoryRepository;
import ch.cern.todo.infrastructure.repository.TaskRepository;
import ch.cern.todo.service.exception.TaskCategoryNotFoundException;
import ch.cern.todo.service.exception.TaskNotFoundException;
import ch.cern.todo.service.exception.UnAuthorizedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    private final CustomUserRepository customUserRepository;

    private final TaskCategoryRepository taskCategoryRepository;

    public TaskService(TaskRepository taskRepository, CustomUserRepository customUserRepository, TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.customUserRepository = customUserRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }


    public Task createTask(Task task, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        TaskCategory taskCategory = taskCategoryRepository.findById(task.getCategory().getId())
                .orElseThrow(() -> new TaskCategoryNotFoundException("Task category not found"));

        Task taskToSave = new Task(task.getId(), task.getName(), task.getDescription(), task.getDeadline(), taskCategory, user);

        return taskRepository.save(taskToSave);
    }

    public Task updateTask(Long taskId, Task taskToUpdate, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        TaskCategory taskCategory = taskCategoryRepository.findById(taskToUpdate.getCategory().getId())
                .orElseThrow(() -> new TaskCategoryNotFoundException("Task category not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task to update not found"));

        if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {

            Task taskUpdated = new Task(taskId, taskToUpdate.getName(), taskToUpdate.getDescription(), taskToUpdate.getDeadline(), taskCategory, user);

            return taskRepository.save(taskUpdated);
        } else {
            throw new UnAuthorizedException("Unauthorized access to the task");
        }
    }

    public Task getTaskById(Long taskId, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {

            return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task not found"));
        } else {
            throw new UnAuthorizedException("Unauthorized access");
        }
    }

    public void deleteTask(Long taskId, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException("Task to update not found"));

        if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {

            taskRepository.deleteById(taskId);
        } else {
            throw new UnAuthorizedException("Unauthorized access");
        }
    }

    public List<Task> getTasksByQuery(TaskQuery taskQuery, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (isAdmin(user) || taskQuery.userId().equals(user.getId())) {

            return taskRepository.findAll(
                    TaskSpecifications.buildTaskQuery(
                            taskQuery.name(),
                            taskQuery.description(),
                            taskQuery.deadline(),
                            taskQuery.categoryId(),
                            taskQuery.userId()
                    )
            );
        } else {
            throw new UnAuthorizedException("Unauthorized access");
        }
    }

    private static boolean isTaskBelongToUser(Long taskId, CustomUser user) {
        return user.getTasks().stream().anyMatch(task -> task.getId().equals(taskId));
    }

    private static boolean isAdmin(CustomUser user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
    }
}

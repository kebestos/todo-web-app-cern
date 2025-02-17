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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ch.cern.todo.service.exception.ExceptionMessage.*;

@Service
@Transactional
public class TaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    private final CustomUserRepository customUserRepository;

    private final TaskCategoryRepository taskCategoryRepository;

    public TaskService(TaskRepository taskRepository, CustomUserRepository customUserRepository, TaskCategoryRepository taskCategoryRepository) {
        this.taskRepository = taskRepository;
        this.customUserRepository = customUserRepository;
        this.taskCategoryRepository = taskCategoryRepository;
    }

    public Task createTask(Task task, String userName) {
        LOGGER.info("createTask is called in TaskService with parameter Task: {} and String userName {}", task, userName);

        CustomUser user = customUserRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));
        LOGGER.info("findByUsername from custom user repository is called with output Custom user: {}", user);

        TaskCategory taskCategory = taskCategoryRepository.findById(task.getCategory().getId())
                .orElseThrow(() -> new TaskCategoryNotFoundException(TASK_CATEGORY_NOT_FOUND.getMessage()));
        LOGGER.info("findById from task category repository is called with output Task Category: {}", taskCategory);

        Task taskToSave = new Task(task.getId(), task.getName(), task.getDescription(), task.getDeadline(), taskCategory, user);

        Task taskSaved = taskRepository.save(taskToSave);
        LOGGER.info("save from task repository is called with parameter Task: {} and output Task: {}", taskToSave, taskSaved);

        return taskSaved;
    }

    public Task updateTask(Long taskId, Task taskToUpdate, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));

        TaskCategory taskCategory = taskCategoryRepository.findById(taskToUpdate.getCategory().getId())
                .orElseThrow(() -> new TaskCategoryNotFoundException(TASK_CATEGORY_NOT_FOUND.getMessage()));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND.getMessage()));

        if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {

            Task taskUpdated = new Task(taskId, taskToUpdate.getName(), taskToUpdate.getDescription(), taskToUpdate.getDeadline(), taskCategory, user);

            return taskRepository.save(taskUpdated);
        } else {
            throw new UnAuthorizedException(UNAUTHORIZED_ACCESS.getMessage());
        }
    }

    public Task getTaskById(Long taskId, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));

        if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {

            return taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND.getMessage()));
        } else {
            throw new UnAuthorizedException(UNAUTHORIZED_ACCESS.getMessage());
        }
    }

    public void deleteTask(Long taskId, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND.getMessage()));

        if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {

            taskRepository.deleteById(taskId);
        } else {
            throw new UnAuthorizedException(UNAUTHORIZED_ACCESS.getMessage());
        }
    }

    public List<Task> getTasksByQuery(TaskQuery taskQuery, String userName) {

        CustomUser user = customUserRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));

        if (isAdmin(user) || taskQuery.userId().equals(user.getId())) {
            return taskRepository.findAll(
                    taskQuery.buildTaskQuery()
            );
        } else {
            throw new UnAuthorizedException(UNAUTHORIZED_ACCESS.getMessage());
        }
    }

    private static boolean isTaskBelongToUser(Long taskId, CustomUser user) {
        return user.getTasks().stream().anyMatch(task -> task.getId().equals(taskId));
    }

    private static boolean isAdmin(CustomUser user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
    }
}

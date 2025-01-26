package ch.cern.todo.service.task;

import ch.cern.todo.infrastructure.entity.Task;
import ch.cern.todo.infrastructure.repository.TaskRepository;
import ch.cern.todo.model.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long taskId, Task taskUpdate) {
        //only user of the task or ADMIN
        Task taskUpdated = new Task(taskId, taskUpdate.getName(), taskUpdate.getDescription(), taskUpdate.getDeadline(), taskUpdate.getCategoryId());
        return taskRepository.save(taskUpdated);
    }

    public Task getTaskById(Long taskId) {

        //get task of user by his username

        //if user is ADMIN or if taskId list contain taskId retrieve

        return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void deleteTask(Long taskId) {

        //only user of the task or ADMIN

        taskRepository.deleteById(taskId);
    }

    public List<Task> getTasksByQuery(TaskQuery taskQuery) {

        //get list of taskId of the user

        return taskRepository.findAll(
                TaskSpecifications.buildTaskQuery(
                        taskQuery.name(),
                        taskQuery.description(),
                        taskQuery.deadline(),
                        taskQuery.categoryId()
                )
        );
    }
}

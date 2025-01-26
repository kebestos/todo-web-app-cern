package ch.cern.todo.service.task;

import ch.cern.todo.infrastructure.entity.CustomUser;
import ch.cern.todo.infrastructure.entity.Task;
import ch.cern.todo.infrastructure.repository.TaskRepository;
import ch.cern.todo.infrastructure.repository.UserRepository;
import ch.cern.todo.model.TaskQuery;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(Task task, String userName) {
        try {
            CustomUser user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            user.getTasks().add(task);

            userRepository.save(user);

            return taskRepository.save(task);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    //TODO is task existing check ?
    public Task updateTask(Long taskId, Task taskUpdate, String userName) {
        //only user of the task or ADMIN
        try {
            CustomUser user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {
                Task taskUpdated = new Task(taskId, taskUpdate.getName(), taskUpdate.getDescription(), taskUpdate.getDeadline(), taskUpdate.getCategory());
                return taskRepository.save(taskUpdated);
            } else {
                throw new RuntimeException("Unauthorized access");
            }
        } catch (Exception e) {
            //TODO add error code
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * TODO separate Exception
     * TODO user can have a list of task
     *
     * @param taskId
     * @param userName
     * @return
     */
    public Task getTaskById(Long taskId, String userName) {
        try {
            CustomUser user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {
                return taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));
            } else {
                throw new RuntimeException("Unauthorized access");
            }
        } catch (Exception e) {
            //TODO add error code
            throw new RuntimeException(e.getMessage());
        }
    }

    //TODO verify task exist  ?
    public void deleteTask(Long taskId, String userName) {
        try {
            CustomUser user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (isAdmin(user) || isTaskBelongToUser(taskId, user)) {
                taskRepository.deleteById(taskId);
            } else {
                throw new RuntimeException("Unauthorized access");
            }
        } catch (Exception e) {
            //TODO add error code
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<Task> getTasksByQuery(TaskQuery taskQuery, String userName) {

        try {
            CustomUser user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));

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
                throw new RuntimeException("Unauthorized access");
            }
        } catch (Exception e) {
            //TODO add error code
            throw new RuntimeException(e.getMessage());
        }
    }

    private static boolean isTaskBelongToUser(Long taskId, CustomUser user) {
        return user.getTasks().stream().anyMatch(task -> task.getId().equals(taskId));
    }

    private static boolean isAdmin(CustomUser user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));
    }
}

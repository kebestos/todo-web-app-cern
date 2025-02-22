package ch.cern.todo.controller.dto.mapper;

import ch.cern.todo.controller.dto.TaskDTO;
import ch.cern.todo.infrastructure.model.Task;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class TaskMapper {

    private final TaskCategoryMapper taskCategoryMapper;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public TaskMapper(TaskCategoryMapper taskCategoryMapper) {
        this.taskCategoryMapper = taskCategoryMapper;
    }

    public TaskDTO toTaskDto(Task task) {

        return new TaskDTO(task.getId(), task.getName(), task.getDescription(), task.getDeadline().format(DATE_TIME_FORMATTER),
                taskCategoryMapper.toTaskCategoryDto(task.getCategory()));
    }

    public Task toTask(TaskDTO taskDto) {

        return new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(),
                LocalDateTime.parse(taskDto.getDeadline(), DATE_TIME_FORMATTER),
                taskCategoryMapper.toTaskCategory(taskDto.getTaskCategory()), null);
    }

    public List<TaskDTO> toTaskDtoList(List<Task> users) {
        return users.stream()
                .map(this::toTaskDto)
                .toList();
    }
}

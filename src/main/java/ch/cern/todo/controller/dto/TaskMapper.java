package ch.cern.todo.controller.dto;

import ch.cern.todo.infrastructure.entity.Task;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * we can also use Model mapper or MapStruct
 */
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

    public Task toTask(TaskDTO taskDto) throws ParseException {

        return new Task(taskDto.getId(), taskDto.getName(), taskDto.getDescription(),
                LocalDateTime.parse(taskDto.getDeadline(), DATE_TIME_FORMATTER),
                taskCategoryMapper.toTaskCategory(taskDto.getTaskCategory()), null);
    }
}

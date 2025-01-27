package ch.cern.todo.controller.dto.mapper;

import ch.cern.todo.controller.dto.TaskQueryDTO;
import ch.cern.todo.domain.TaskQuery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskQueryMapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public TaskQuery toTaskQuery(TaskQueryDTO taskQueryDto){
        return new TaskQuery(taskQueryDto.getName(),taskQueryDto.getDescription(),
                LocalDateTime.parse(taskQueryDto.getDeadline(), DATE_TIME_FORMATTER), taskQueryDto.getCategoryId(),taskQueryDto.getUserId());
    }

    public TaskQueryDTO toTaskQueryDto(TaskQuery taskQuery){
        return new TaskQueryDTO(taskQuery.name(),taskQuery.description(),
                taskQuery.deadline().format(DATE_TIME_FORMATTER), taskQuery.categoryId(),taskQuery.userId());
    }
}

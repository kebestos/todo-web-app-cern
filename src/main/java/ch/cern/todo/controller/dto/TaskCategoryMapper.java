package ch.cern.todo.controller.dto;

import ch.cern.todo.infrastructure.entity.TaskCategory;
import org.springframework.stereotype.Component;

@Component
public class TaskCategoryMapper {

    public TaskCategoryDTO toTaskCategoryDto(TaskCategory taskCategory) {
        return new TaskCategoryDTO(taskCategory.getId(), taskCategory.getName(), taskCategory.getDescription());
    }

    public TaskCategory toTaskCategory(TaskCategoryDTO taskCategoryDto) {
        return new TaskCategory(taskCategoryDto.getId(), taskCategoryDto.getName(), taskCategoryDto.getDescription());
    }
}

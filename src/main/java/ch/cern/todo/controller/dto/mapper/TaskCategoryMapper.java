package ch.cern.todo.controller.dto.mapper;

import ch.cern.todo.controller.dto.TaskCategoryDTO;
import ch.cern.todo.infrastructure.model.TaskCategory;
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

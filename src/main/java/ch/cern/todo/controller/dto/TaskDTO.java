package ch.cern.todo.controller.dto;

import org.springframework.format.annotation.DateTimeFormat;

public class TaskDTO {

    private Long id;

    private String name;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String deadline;

    private TaskCategoryDTO taskCategory;

    public TaskDTO(Long id, String name, String description, String deadline, TaskCategoryDTO taskCategory) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.taskCategory = taskCategory;
    }

    public TaskDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public TaskCategoryDTO getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(TaskCategoryDTO taskCategory) {
        this.taskCategory = taskCategory;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                ", taskCategory=" + taskCategory +
                '}';
    }
}

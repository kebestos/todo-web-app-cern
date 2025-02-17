package ch.cern.todo.controller.dto;

import org.springframework.format.annotation.DateTimeFormat;

public class TaskQueryDTO {

    private String name;

    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private String deadline;

    private Long categoryId;

    private Long userId;

    private String deadlineCriteria;

    public TaskQueryDTO(String name, String description, String deadline, Long categoryId, Long userId, String deadlineCriteria) {
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.categoryId = categoryId;
        this.userId = userId;
        this.deadlineCriteria = deadlineCriteria;
    }

    public TaskQueryDTO() {
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDeadlineCriteria() {
        return deadlineCriteria;
    }

    public void setDeadlineCriteria(String deadlineCriteria) {
        this.deadlineCriteria = deadlineCriteria;
    }

    @Override
    public String toString() {
        return "TaskQueryDTO{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", deadline='" + deadline + '\'' +
                ", categoryId=" + categoryId +
                ", userId=" + userId +
                ", deadlineCriteria='" + deadlineCriteria + '\'' +
                '}';
    }
}

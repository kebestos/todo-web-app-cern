package ch.cern.todo.infrastructure.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Long id;

    private String name;

    private String description;

    private LocalDateTime deadline;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private TaskCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private CustomUser user;

    public Task(Long id, String name, String description, LocalDateTime deadline, TaskCategory category, CustomUser user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.category = category;
        this.user = user;
    }

    public Task() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public TaskCategory getCategory() {
        return category;
    }

    public CustomUser getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(deadline, task.deadline) && Objects.equals(category, task.category) && Objects.equals(user, task.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, deadline, category, user);
    }

    @Override
    public String toString() {
        return '\n' + "Task{" +
                "id=" + id + '\n' +
                ", name='" + name + '\'' + '\n' +
                ", description='" + description + '\'' + '\n' +
                ", deadline=" + deadline + '\n' +
                ", category=" + category + '\n' +
                ", user=" + user + '}' + '\n' ;
    }
}

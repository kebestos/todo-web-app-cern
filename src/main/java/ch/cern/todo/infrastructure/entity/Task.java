package ch.cern.todo.infrastructure.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Task
//        (@Id
//                   @GeneratedValue(strategy = GenerationType.IDENTITY)
//                   Long id,
//
////                   @Column(nullable = false)
//                   String name,
//
//                   String description,
//
////                   @Temporal(TemporalType.TIME)
////                   @Column(nullable = false)
//                   Date deadline,
//
////                   @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
////                   @JoinTable(
////                           name = "category",
////                           joinColumns = @JoinColumn(name = "task_id"),
////                           inverseJoinColumns = @JoinColumn(name = "task_category_id")
////                   )
//                   Long categoryId)
{
//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "category",
//            joinColumns = @JoinColumn(name = "task_id"),
//            inverseJoinColumns = @JoinColumn(name = "task_category_id")
//    )
//    TaskCategory category;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Timestamp deadline;
    private Long categoryId;

    public Task(Long id, String name, String description, Timestamp deadline, Long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.categoryId = categoryId;
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

    public Timestamp getDeadline() {
        return deadline;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(deadline, task.deadline) && Objects.equals(categoryId, task.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, deadline, categoryId);
    }
}

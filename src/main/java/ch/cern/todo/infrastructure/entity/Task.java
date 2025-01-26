package ch.cern.todo.infrastructure.entity;

import jakarta.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Long id;

    private String name;

    private String description;

    private Timestamp deadline;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private TaskCategory category;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private CustomUser user;

    public Task(Long id, String name, String description, Timestamp deadline, TaskCategory category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.category = category;
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

    public TaskCategory getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && Objects.equals(deadline, task.deadline) && Objects.equals(category, task.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, deadline, category);
    }
}

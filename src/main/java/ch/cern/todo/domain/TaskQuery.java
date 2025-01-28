package ch.cern.todo.domain;

import ch.cern.todo.infrastructure.model.Task;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record TaskQuery(String name, String description, LocalDateTime deadline, Long categoryId, Long userId,
                        String deadlineCriteria) {

    public static Specification<Task> buildTaskQuery(String taskName, String taskDescription,
                                                     LocalDateTime taskDeadline, Long taskCategoryId, Long userId, String deadlineCriteria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null) {
                root.fetch("user", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(root.get("user").get("Id"), userId));
            }
            if (taskName != null && !taskName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + taskName.toLowerCase() + "%"));
            }
            if (taskDescription != null && !taskDescription.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + taskDescription.toLowerCase() + "%"));
            }
            if (taskDeadline != null && deadlineCriteria != null && deadlineCriteria.equals("EQUAL")) {
                predicates.add(criteriaBuilder.equal(root.<LocalDateTime>get("deadline"), taskDeadline));
            }
            if (taskDeadline != null && deadlineCriteria != null && deadlineCriteria.equals("GREATER")) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<LocalDateTime>get("deadline"), taskDeadline));
            }
            if (taskDeadline != null && deadlineCriteria != null && deadlineCriteria.equals("LESS")) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<LocalDateTime>get("deadline"), taskDeadline));
            }
            if (taskCategoryId != null) {
                root.fetch("category", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(root.get("category").get("Id"), taskCategoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

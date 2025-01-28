package ch.cern.todo.domain;

import ch.cern.todo.infrastructure.model.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record TaskQuery(String name, String description, LocalDateTime deadline, Long categoryId, Long userId) {

    public static Specification<Task> buildTaskQuery(String taskName, String taskDescription, LocalDateTime taskDeadline, Long taskCategoryId, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //todo convert localdatetime to date for the predicat
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user_id"), userId));
            }
            if (taskName != null && !taskName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + taskName.toLowerCase() + "%"));
            }
            if (taskDescription != null && !taskDescription.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + taskDescription.toLowerCase() + "%"));
            }
            if (taskDeadline != null) {
                predicates.add(criteriaBuilder.equal(root.<LocalDateTime>get("deadline"), taskDeadline));
            }
            if (taskCategoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category_id"), taskCategoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

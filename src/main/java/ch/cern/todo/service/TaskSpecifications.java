package ch.cern.todo.service;

import ch.cern.todo.infrastructure.model.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskSpecifications {

    public static Specification<Task> buildTaskQuery(String taskName, String taskDescription, Date taskDeadline, Long taskCategoryId, Long userId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(userId != null){
                predicates.add(criteriaBuilder.equal(root.get("user_id"), userId));
            }
            if (taskName != null && !taskName.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + taskName.toLowerCase() + "%"));
            }
            if (taskDescription != null && !taskDescription.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + taskDescription.toLowerCase() + "%"));
            }
            if (taskDeadline != null) {
                predicates.add(criteriaBuilder.equal(root.<Timestamp>get("deadline"), taskDeadline));
            }
            if (taskCategoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category_id"), taskCategoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

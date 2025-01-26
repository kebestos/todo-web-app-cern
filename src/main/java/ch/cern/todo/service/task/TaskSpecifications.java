package ch.cern.todo.service.task;

import ch.cern.todo.infrastructure.entity.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskSpecifications {

    public static Specification<Task> buildTaskQuery(String taskName, String taskDescription, Date taskDeadline, Long taskCategoryId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            //

            if (taskName != null && !taskName.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get(taskName)));
            }
            if (taskDescription != null && !taskDescription.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get(taskDescription)));
            }
            if (taskDeadline != null) {
                predicates.add(criteriaBuilder.equal(root.<Date>get("deadline"), taskDeadline));
            }
            if (taskCategoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), taskCategoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

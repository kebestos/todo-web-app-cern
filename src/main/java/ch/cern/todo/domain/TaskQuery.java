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

    public Specification<Task> buildTaskQuery() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (this.userId != null) {
                root.fetch("user", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(root.get("user").get("Id"), this.userId));
            }
            if (this.name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + this.name.toLowerCase() + "%"));
            }
            if (this.description != null && !this.description.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + this.description.toLowerCase() + "%"));
            }
            if (this.deadline != null && this.deadlineCriteria != null && this.deadlineCriteria.equals("EQUAL")) {
                LocalDateTime localDateTimeBefore = LocalDateTime.of(deadline.getYear(),deadline.getMonth(),deadline.getDayOfMonth(),0,0,0);
                LocalDateTime localDateTimeAfter =  LocalDateTime.of(deadline.getYear(),deadline.getMonth(),deadline.getDayOfMonth(),23,59,59);
                predicates.add(criteriaBuilder.between(root.<LocalDateTime>get("deadline"), localDateTimeBefore,localDateTimeAfter));
            }
            if (this.deadline != null && this.deadlineCriteria != null && this.deadlineCriteria.equals("GREATER")) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.<LocalDateTime>get("deadline"), this.deadline));
            }
            if (this.deadline != null && this.deadlineCriteria != null && this.deadlineCriteria.equals("LESS")) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.<LocalDateTime>get("deadline"), this.deadline));
            }
            if (this.categoryId != null) {
                root.fetch("category", JoinType.LEFT);
                predicates.add(criteriaBuilder.equal(root.get("category").get("Id"), this.categoryId));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

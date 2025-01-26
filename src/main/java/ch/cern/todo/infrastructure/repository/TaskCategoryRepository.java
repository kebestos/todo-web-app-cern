package ch.cern.todo.infrastructure.repository;

import ch.cern.todo.infrastructure.entity.TaskCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskCategoryRepository  extends JpaRepository<TaskCategory, Long> {
}

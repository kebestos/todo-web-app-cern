package ch.cern.todo.infrastructure.repository;

import ch.cern.todo.infrastructure.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}

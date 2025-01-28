package ch.cern.todo.domain;

import ch.cern.todo.infrastructure.model.Task;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TaskQueryTest {

    @Mock
    private Root<Task> root;

    @Mock
    private CriteriaQuery<Task> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Predicate mockPredicate;

    @Mock
    Path<Object> path;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock the methods of CriteriaBuilder to return mockPredicate
        when(criteriaBuilder.like(any(Expression.class), anyString())).thenReturn(mockPredicate);
        when(criteriaBuilder.equal(any(Expression.class), any())).thenReturn(mockPredicate);
        when(criteriaBuilder.greaterThanOrEqualTo(any(Expression.class), any(LocalDateTime.class))).thenReturn(mockPredicate);
        when(criteriaBuilder.lessThanOrEqualTo(any(Expression.class), any(LocalDateTime.class))).thenReturn(mockPredicate);
        when(criteriaBuilder.and(any(Predicate[].class))).thenReturn(mockPredicate);
    }

    @Test
    void testBuildTaskQuery_WithNameAndDescription() {
        // Arrange
        String name = "Test Task";
        String description = "Description";
        TaskQuery taskQuery = new TaskQuery(name, description, null, null, null, null);

        when(root.get("name")).thenReturn(mock(Path.class));
        when(root.get("description")).thenReturn(mock(Path.class));

        // Act
        Specification<Task> specification = taskQuery.buildTaskQuery();
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Assert
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).like(any(), eq("%" + name.toLowerCase() + "%"));
        verify(criteriaBuilder, times(1)).like(any(), eq("%" + description.toLowerCase() + "%"));
    }

    @Test
    void testBuildTaskQuery_WithDeadlineAndCriteria() {
        // Arrange
        LocalDateTime deadline = LocalDateTime.now();
        String deadlineCriteria = "GREATER";
        TaskQuery taskQuery = new TaskQuery(null, null, deadline, null, null, deadlineCriteria);

        when(root.get("deadline")).thenReturn(mock(Path.class));

        // Act
        Specification<Task> specification = taskQuery.buildTaskQuery();
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Assert
        assertNotNull(result);
        verify(criteriaBuilder, times(1)).greaterThanOrEqualTo(any(), eq(deadline));
    }

    @Test
    void testBuildTaskQuery_WithUserId() {
        // Arrange
        Long userId = 1L;
        TaskQuery taskQuery = new TaskQuery(null, null, null, null, userId, null);

        when(root.get(anyString())).thenReturn(path);
        when(path.get(anyString())).thenReturn(mock(Path.class));

        // Act
        Specification<Task> specification = taskQuery.buildTaskQuery();
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Assert
        assertNotNull(result);
        verify(root, times(1)).fetch("user", JoinType.LEFT);
        verify(criteriaBuilder, times(1)).equal(any(), eq(userId));
    }

    @Test
    void testBuildTaskQuery_WithCategoryId() {
        // Arrange
        Long categoryId = 2L;
        TaskQuery taskQuery = new TaskQuery(null, null, null, categoryId, null, null);

        when(root.get(anyString())).thenReturn(path);
        when(path.get(anyString())).thenReturn(mock(Path.class));

        // Act
        Specification<Task> specification = taskQuery.buildTaskQuery();
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Assert
        assertNotNull(result);
        verify(root, times(1)).fetch("category", JoinType.LEFT);
        verify(criteriaBuilder, times(1)).equal(any(), eq(categoryId));
    }

    @Test
    void testBuildTaskQuery_EmptyQuery() {
        // Arrange
        TaskQuery taskQuery = new TaskQuery(null, null, null, null, null, null);

        // Act
        Specification<Task> specification = taskQuery.buildTaskQuery();
        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        // Assert
        assertNotNull(result);
    }
}
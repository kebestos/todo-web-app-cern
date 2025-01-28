package ch.cern.todo.infrastructure.model;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomUserTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String username = "testUser";
        String password = "testPassword";
        Set<Role> roles = Set.of(new Role(1L, "ADMIN"), new Role(2L, "USER"));
        List<Task> tasks = List.of(new Task(1L, "Task 1", "Description 1", null, null, null));

        // Act
        CustomUser customUser = new CustomUser(id, username, password, roles, tasks);

        // Assert
        assertEquals(id, customUser.getId());
        assertEquals(username, customUser.getUsername());
        assertEquals(password, customUser.getPassword());
        assertEquals(roles, customUser.getRoles());
        assertEquals(tasks, customUser.getTasks());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        CustomUser user1 = new CustomUser(1L, "testUser", "testPassword", Set.of(new Role(1L, "ADMIN")), List.of());
        CustomUser user2 = new CustomUser(1L, "testUser", "testPassword", Set.of(new Role(1L, "ADMIN")), List.of());

        // Act & Assert
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void testNotEquals() {
        // Arrange
        CustomUser user1 = new CustomUser(1L, "testUser", "testPassword", Set.of(new Role(1L, "ADMIN")), List.of());
        CustomUser user2 = new CustomUser(2L, "anotherUser", "anotherPassword", Set.of(new Role(2L, "USER")), List.of());

        // Act & Assert
        assertNotEquals(user1, user2);
    }

}
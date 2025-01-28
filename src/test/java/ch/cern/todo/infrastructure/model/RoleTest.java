package ch.cern.todo.infrastructure.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class RoleTest {

    @Test
    void testConstructorAndGetters() {
        // Arrange
        Long id = 1L;
        String name = "Admin";

        // Act
        Role role = new Role(id, name);

        // Assert
        assertEquals(id, role.getId());
        assertEquals(name, role.getName());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Role role1 = new Role(1L, "Admin");
        Role role2 = new Role(1L, "Admin");

        // Act & Assert
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void testNotEquals() {
        // Arrange
        Role role1 = new Role(1L, "Admin");
        Role role2 = new Role(2L, "User");

        // Act & Assert
        assertNotEquals(role1, role2);
    }
}
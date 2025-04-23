package ru.practicum.shareit.user;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTests {


    @Test
    void testAllArgsConstructor() {
        User user = new User(1L, "Test User", "test@email.com");

        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@email.com", user.getEmail());
    }

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testBuilder() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@email.com")
                .build();

        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@email.com", user.getEmail());
    }

    @Test
    void testToString() {
        User user = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@email.com")
                .build();

        String expectedString = "User(id=1, name=Test User, email=test@email.com)";
        assertEquals(expectedString, user.toString());
    }

    @Test
    void testSetters() {
        User user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@email.com");

        assertEquals(1L, user.getId());
        assertEquals("Test User", user.getName());
        assertEquals("test@email.com", user.getEmail());
    }

    @Test
    void testEntityAnnotations() throws NoSuchFieldException {
        // Test @Entity
        assertTrue(User.class.isAnnotationPresent(Entity.class));

        // Test @Table
        Table tableAnnotation = User.class.getAnnotation(Table.class);
        assertNotNull(tableAnnotation);
        assertEquals("users", tableAnnotation.name());
        assertEquals("public", tableAnnotation.schema());

        // Test @Id and @GeneratedValue
        assertNotNull(User.class.getDeclaredField("id").getAnnotation(Id.class));
        GeneratedValue generatedValue = User.class.getDeclaredField("id").getAnnotation(GeneratedValue.class);
        assertNotNull(generatedValue);
        assertEquals(GenerationType.IDENTITY, generatedValue.strategy());

        // Test @Column for name
        Column nameColumn = User.class.getDeclaredField("name").getAnnotation(Column.class);
        assertNotNull(nameColumn);
        assertFalse(nameColumn.nullable());

        // Test @Column for email
        Column emailColumn = User.class.getDeclaredField("email").getAnnotation(Column.class);
        assertNotNull(emailColumn);
        assertFalse(emailColumn.nullable());
        assertTrue(emailColumn.unique());
    }

    @Test
    void equals_ShouldReturnFalseForDifferentIds() {
        User user1 = User.builder().id(1L).build();
        User user2 = User.builder().id(2L).build();

        assertNotEquals(user1, user2);
    }

    @Test
    void equals_ShouldReturnFalseForNull() {
        User user = User.builder().id(1L).build();

        assertNotEquals(null, user);
    }

    @Test
    void equals_ShouldReturnFalseForDifferentClass() {
        User user = User.builder().id(1L).build();

        assertNotEquals(user, new Object());
    }

    @Test
    void equals_ShouldReturnTrueForSameInstance() {
        User user = User.builder().id(1L).build();

        assertEquals(user, user);
    }

    @Test
    void hashCode_ShouldBeConsistent() {
        User user = User.builder().id(1L).build();
        int initialHashCode = user.hashCode();

        assertEquals(initialHashCode, user.hashCode());
    }
}

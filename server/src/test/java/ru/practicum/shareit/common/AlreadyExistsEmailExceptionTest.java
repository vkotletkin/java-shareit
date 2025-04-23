package ru.practicum.shareit.common;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.AlreadyExistsEmailException;
import ru.practicum.shareit.exception.ExceptionsGlobalHandler;
import ru.practicum.shareit.exception.response.BaseErrorResponse;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AlreadyExistsEmailExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {

        String message = "Email already exists";
        AlreadyExistsEmailException exception = new AlreadyExistsEmailException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithFormattedMessage() {

        String email = "test@example.com";
        String expectedMessage = "Email test@example.com already exists";
        AlreadyExistsEmailException exception = new AlreadyExistsEmailException(
                "Email {0} already exists", email);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionSupplier() {

        String message = "Email already exists";
        Supplier<AlreadyExistsEmailException> supplier =
                AlreadyExistsEmailException.alreadyExistsEmailException(message);

        assertNotNull(supplier);
        assertEquals(message, supplier.get().getMessage());
    }

    @Test
    void shouldHandleExceptionInGlobalHandler() {

        ExceptionsGlobalHandler handler = new ExceptionsGlobalHandler();
        String message = "Email conflict";
        AlreadyExistsEmailException exception = new AlreadyExistsEmailException(message);

        BaseErrorResponse response = handler.handleAlreadyExistsEmailException(exception);

        assertEquals("Такое значение уже используется.", response.error());
        assertEquals(message, response.description());
    }
}

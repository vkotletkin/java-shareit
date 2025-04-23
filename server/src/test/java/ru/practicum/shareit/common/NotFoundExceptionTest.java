package ru.practicum.shareit.common;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ExceptionsGlobalHandler;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.response.BaseErrorResponse;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Not found";
        NotFoundException exception = new NotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithFormattedMessage() {
        Long userId = 789L;
        String expectedMessage = "User 789 not found";
        NotFoundException exception = new NotFoundException(
                "User {0} not found", userId);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionSupplier() {
        String message = "Not found";
        Supplier<NotFoundException> supplier =
                NotFoundException.notFoundException(message);

        assertNotNull(supplier);
        assertEquals(message, supplier.get().getMessage());
    }

    @Test
    void shouldHandleExceptionInGlobalHandler() {
        ExceptionsGlobalHandler handler = new ExceptionsGlobalHandler();
        String message = "Resource not found";
        NotFoundException exception = new NotFoundException(message);

        BaseErrorResponse response = handler.handleNotFoundException(exception);

        assertEquals("Объект не найден.", response.error());
        assertEquals(message, response.description());
    }
}
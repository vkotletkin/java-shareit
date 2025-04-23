package ru.practicum.shareit.common;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ExceptionsGlobalHandler;
import ru.practicum.shareit.exception.IncorrectOwnerException;
import ru.practicum.shareit.exception.response.BaseErrorResponse;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class IncorrectOwnerExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Incorrect owner";
        IncorrectOwnerException exception = new IncorrectOwnerException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithFormattedMessage() {
        Long ownerId = 123L;
        String expectedMessage = "User 123 is not the owner";
        IncorrectOwnerException exception = new IncorrectOwnerException(
                "User {0} is not the owner", ownerId);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionSupplier() {
        String message = "Incorrect owner";
        Supplier<IncorrectOwnerException> supplier =
                IncorrectOwnerException.incorrectOwnerException(message);

        assertNotNull(supplier);
        assertEquals(message, supplier.get().getMessage());
    }

    @Test
    void shouldHandleExceptionInGlobalHandler() {
        ExceptionsGlobalHandler handler = new ExceptionsGlobalHandler();
        String message = "Owner validation failed";
        IncorrectOwnerException exception = new IncorrectOwnerException(message);

        BaseErrorResponse response = handler.handleIncorrectOwnerException(exception);

        assertEquals("Ошибка при передаче идентификатора владельца предмета", response.error());
        assertEquals(message, response.description());
    }
}

package ru.practicum.shareit.common;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ExceptionsGlobalHandler;
import ru.practicum.shareit.exception.NotAvailableItemException;
import ru.practicum.shareit.exception.response.BaseErrorResponse;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NotAvailableItemExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {

        String message = "Item not available";
        NotAvailableItemException exception = new NotAvailableItemException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithFormattedMessage() {

        long itemId = 456L;
        String expectedMessage = "Item 456 is not available";
        NotAvailableItemException exception = new NotAvailableItemException(
                "Item {0} is not available", itemId);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionSupplier() {

        String message = "Item not available";
        Supplier<NotAvailableItemException> supplier =
                NotAvailableItemException.notAvailableItemException(message);

        assertNotNull(supplier);
        assertEquals(message, supplier.get().getMessage());
    }

    @Test
    void shouldHandleExceptionInGlobalHandler() {

        ExceptionsGlobalHandler handler = new ExceptionsGlobalHandler();
        String message = "Item availability check failed";
        NotAvailableItemException exception = new NotAvailableItemException(message);

        BaseErrorResponse response = handler.handleNotAvailableItemException(exception);

        assertEquals("Ошибка при получении предмета", response.error());
        assertEquals(message, response.description());
    }
}

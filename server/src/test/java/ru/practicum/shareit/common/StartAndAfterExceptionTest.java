package ru.practicum.shareit.common;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ExceptionsGlobalHandler;
import ru.practicum.shareit.exception.StartAfterEndException;
import ru.practicum.shareit.exception.response.BaseErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StartAndAfterExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String message = "Start after end";
        StartAfterEndException exception = new StartAfterEndException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithFormattedMessage() {
        String start = "2023-01-01";
        String end = "2022-12-31";
        String expectedMessage = "Start date 2023-01-01 is after end date 2022-12-31";
        StartAfterEndException exception = new StartAfterEndException(
                "Start date {0} is after end date {1}", start, end);

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void shouldHandleExceptionInGlobalHandler() {
        ExceptionsGlobalHandler handler = new ExceptionsGlobalHandler();
        String message = "Invalid date range";
        StartAfterEndException exception = new StartAfterEndException(message);

        BaseErrorResponse response = handler.handleStartAfterEndException(exception);

        assertEquals("Ошибка при передаче временных промежутков", response.error());
        assertEquals(message, response.description());
    }
}

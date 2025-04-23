package ru.practicum.shareit.common;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exception.ExceptionsGlobalHandler;
import ru.practicum.shareit.exception.response.BaseErrorResponse;
import ru.practicum.shareit.exception.response.ValidationErrorResponse;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ConstraintViolationExceptionTest {

    @Test
    void shouldHandleConstraintViolationException() {
        ExceptionsGlobalHandler handler = new ExceptionsGlobalHandler();

        // Создаем мок для ConstraintViolation
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("email");
        when(violation.getMessage()).thenReturn("must be a well-formed email address");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(violation);

        ConstraintViolationException exception = new ConstraintViolationException(
                "Validation failed", violations);

        ValidationErrorResponse response = handler.handleOnConstraintValidationException(exception);

        assertEquals(1, response.error().size());
        BaseErrorResponse error = response.error().get(0);
        assertEquals("email", error.error());
        assertEquals("must be a well-formed email address", error.description());
    }
}

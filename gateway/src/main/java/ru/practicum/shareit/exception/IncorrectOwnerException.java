package ru.practicum.shareit.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class IncorrectOwnerException extends RuntimeException {
    public IncorrectOwnerException(String message) {
        super(message);
    }

    public IncorrectOwnerException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<IncorrectOwnerException> incorrectOwnerException(String message, Object... args) {
        return () -> new IncorrectOwnerException(message, args);
    }

    public static Supplier<IncorrectOwnerException> incorrectOwnerException(String message) {
        return () -> new IncorrectOwnerException(message);
    }
}

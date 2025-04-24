package ru.practicum.shareit.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class AlreadyExistsEmailException extends RuntimeException {
    public AlreadyExistsEmailException(String message) {
        super(message);
    }

    public AlreadyExistsEmailException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<AlreadyExistsEmailException> alreadyExistsEmailException(String message) {
        return () -> new AlreadyExistsEmailException(message);
    }
}

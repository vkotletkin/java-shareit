package ru.practicum.shareit.exception;

import java.text.MessageFormat;

public class StartAfterEndException extends RuntimeException {
    public StartAfterEndException(String message) {
        super(message);
    }

    public StartAfterEndException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }
}

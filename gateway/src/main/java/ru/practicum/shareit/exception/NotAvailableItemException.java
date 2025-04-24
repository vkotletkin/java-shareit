package ru.practicum.shareit.exception;

import java.text.MessageFormat;
import java.util.function.Supplier;

public class NotAvailableItemException extends RuntimeException {
    public NotAvailableItemException(String message) {
        super(message);
    }

    public NotAvailableItemException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }

    public static Supplier<NotAvailableItemException> notAvailableItemException(String message, Object... args) {
        return () -> new NotAvailableItemException(message, args);
    }

    public static Supplier<NotAvailableItemException> notAvailableItemException(String message) {
        return () -> new NotAvailableItemException(message);
    }
}

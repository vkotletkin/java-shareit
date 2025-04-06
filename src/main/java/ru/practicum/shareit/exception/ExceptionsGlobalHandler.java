package ru.practicum.shareit.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.response.BaseErrorResponse;
import ru.practicum.shareit.exception.response.ValidationErrorResponse;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionsGlobalHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public BaseErrorResponse handleNotFoundException(final NotFoundException e) {
        log.error(e.getMessage());
        return new BaseErrorResponse("Объект не найден.", e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public BaseErrorResponse handleAlreadyExistsEmailException(final AlreadyExistsEmailException e) {
        log.error(e.getMessage());
        return new BaseErrorResponse("Такое значение уже используется.", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ValidationErrorResponse handleOnConstraintValidationException(
            ConstraintViolationException e) {
        final List<BaseErrorResponse> errorResponses = e.getConstraintViolations().stream()
                .map(error -> new BaseErrorResponse(
                                error.getPropertyPath().toString(),
                                error.getMessage()
                        )
                )
                .toList();

        log.error(e.getMessage());

        return new ValidationErrorResponse(errorResponses);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ValidationErrorResponse handleOnMethodArgumentNotValidException(
            final MethodArgumentNotValidException e) {
        final List<BaseErrorResponse> errorResponses = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new BaseErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        log.error(e.getMessage());

        return new ValidationErrorResponse(errorResponses);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public BaseErrorResponse handleMissingRequestHeaderException(final MissingRequestHeaderException e) {
        log.error(e.getMessage());
        return new BaseErrorResponse("Ошибка передачи заголовка.", e.getHeaderName());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public BaseErrorResponse handleIncorrectOwnerException(final IncorrectOwnerException e) {
        log.error(e.getMessage());
        return new BaseErrorResponse("Ошибка при передаче идентификатора владельца предмета", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public BaseErrorResponse handleStartAfterEndException(final StartAfterEndException e) {
        log.error(e.getMessage());
        return new BaseErrorResponse("Ошибка при передаче временных промежутоков", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public BaseErrorResponse handleNotAvailableItemException(final NotAvailableItemException e) {
        log.error(e.getMessage());
        return new BaseErrorResponse("Ошибка при получении предмета", e.getMessage());
    }
}

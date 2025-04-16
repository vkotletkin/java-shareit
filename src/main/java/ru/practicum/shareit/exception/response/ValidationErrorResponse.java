package ru.practicum.shareit.exception.response;

import java.util.List;

public record ValidationErrorResponse(List<BaseErrorResponse> error) {
}

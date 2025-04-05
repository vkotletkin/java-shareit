package ru.practicum.shareit.booking.dto;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingState;

import java.time.Instant;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;

    @NotNull
    @FutureOrPresent(message = "Дата должна быть в настоящем или будущем")
    Instant start;

    @NotNull
    @FutureOrPresent(message = "Дата должна быть в настоящем или будущем")
    Instant end;

    @NotNull
    Long itemId;

    Long userId;

    @NotNull
    BookingState state;
}

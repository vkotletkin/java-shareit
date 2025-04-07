package ru.practicum.shareit.booking.dto;


import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.common.dto.CatalogDto;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    Long id;

    @NotNull
    @Future(message = "Дата должна быть в настоящем или будущем")
    LocalDateTime start;

    @NotNull
    @Future(message = "Дата должна быть в настоящем или будущем")
    LocalDateTime end;

    CatalogDto booker;

    CatalogDto item;

    @NotNull
    BookingStatus status;
}

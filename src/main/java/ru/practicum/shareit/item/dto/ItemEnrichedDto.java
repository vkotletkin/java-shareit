package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemEnrichedDto {
    Long id;

    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;

    Long ownerId;

    Long requestId;

    LocalDateTime lastBooking;

    LocalDateTime nextBooking;
}

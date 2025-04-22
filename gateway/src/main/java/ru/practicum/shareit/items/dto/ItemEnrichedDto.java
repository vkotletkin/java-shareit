package ru.practicum.shareit.items.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

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

    List<String> comments;
}

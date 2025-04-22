package ru.practicum.shareit.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.items.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PACKAGE)
public class ItemRequestDto {
    Long id;

    @NotBlank
    String description;

    Long requestor;

    LocalDateTime created;

    List<ItemDto> items;
}

package ru.practicum.shareit.items.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {
    Long id;

    @NotBlank
    String text;

    String authorName;

    LocalDateTime created;
}
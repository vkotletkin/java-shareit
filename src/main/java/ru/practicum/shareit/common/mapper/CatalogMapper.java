package ru.practicum.shareit.common.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.common.dto.CatalogDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CatalogMapper {

    public static CatalogDto mapToDto(User user) {
        return CatalogDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static CatalogDto mapToDto(Item item) {
        return CatalogDto.builder()
                .id(item.getId())
                .name(item.getName())
                .build();
    }
}

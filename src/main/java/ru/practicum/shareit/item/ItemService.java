package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto create(ItemDto itemDto);

    Collection<ItemDto> findAllItemsByUser(long userId);

    ItemDto findById(long id);

    ItemDto update(ItemDto itemDto);

    Collection<ItemDto> findByText(String text);
}

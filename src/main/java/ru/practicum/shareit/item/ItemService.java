package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemEnrichedDto;

import java.util.Collection;

public interface ItemService {
    ItemDto create(ItemDto itemDto);

    Collection<ItemDto> findAllItemsByUser(long userId);

    ItemEnrichedDto findById(long id);

    ItemDto update(ItemDto itemDto);

    Collection<ItemDto> findByText(String text);

    CommentDto createComment(CommentDto commentDto, long itemId, long userId);
}

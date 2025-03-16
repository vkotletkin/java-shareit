package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final static String USER_NOT_FOUND_MESSAGE = "Пользователь с идентификатором {0} не найден";

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto itemDto) {
        Item item = ItemMapper.mapToModel(itemDto);
        checkItemExistsUser(item);
        item = itemRepository.create(item);
        return ItemMapper.mapToDto(item);
    }

    @Override
    public Collection<ItemDto> findAllOnUser(long userId) {
        return itemRepository.findAllByUser(userId).stream().map(ItemMapper::mapToDto).collect(Collectors.toList());
    }


    @Override
    public ItemDto findById(long id) {
        return ItemMapper.mapToDto(itemRepository.findById(id).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE)));
    }

    @Override
    public ItemDto update(ItemDto itemDto) {
        Item item = ItemMapper.mapToModel(itemDto);
        Item oldItem = itemRepository.findById(item.getId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Item result = update(oldItem, item);
        itemRepository.update(result);
        return ItemMapper.mapToDto(result);
    }

    @Override
    public Collection<ItemDto> findByText(String text, long userId) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findByText(text).stream().map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    private void checkItemExistsUser(Item item) {
        userRepository.findById(item.getOwnerId())
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, item.getOwnerId()));
    }

    private Item update(Item oldFilm, Item newFilm) {
        return Item.builder()
                .id(newFilm.getId())
                .name(newFilm.getName() == null ? oldFilm.getName() : newFilm.getName())
                .description(newFilm.getDescription() == null ? oldFilm.getDescription() : newFilm.getDescription())
                .isAvailable(newFilm.isAvailable())
                .ownerId(newFilm.getOwnerId() == null ? oldFilm.getOwnerId() : newFilm.getOwnerId())
                .requestId(newFilm.getRequestId() == null ? oldFilm.getRequestId() : newFilm.getRequestId())
                .build();
    }
}

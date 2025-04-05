package ru.practicum.shareit.item.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь с идентификатором {0} не найден";

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemDto create(ItemDto itemDto) {
        User user = userRepository.findById(itemDto.getOwnerId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Item item = ItemMapper.mapToModel(itemDto, user);
        item = itemRepository.save(item);
        return ItemMapper.mapToDto(item);
    }

    @Override
    public Collection<ItemDto> findAllItemsByUser(long userId) {
        User user = userRepository.findById(userId).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        return itemRepository.findByOwnerIdEquals(user).stream().map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto findById(long id) {
        return ItemMapper.mapToDto(itemRepository.findById(id).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE)));
    }

    @Override
    public ItemDto update(ItemDto itemDto) {
        User user = userRepository.findById(itemDto.getOwnerId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Item item = ItemMapper.mapToModel(itemDto, user);
        Item oldItem = itemRepository.findById(item.getId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        Item result = update(oldItem, item);
        itemRepository.save(result);
        return ItemMapper.mapToDto(result);
    }

    @Override
    public Collection<ItemDto> findByText(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findTextNameAndDescription(text)
                .stream().filter(Item::getAvailable).map(ItemMapper::mapToDto).collect(Collectors.toList());
    }

    private Item update(Item oldFilm, Item newFilm) {
        Item item = new Item();
        item.setId(newFilm.getId());
        item.setName(newFilm.getName() == null ? oldFilm.getName() : newFilm.getName());
        item.setDescription(newFilm.getDescription() == null ? oldFilm.getDescription() : newFilm.getDescription());
        item.setAvailable(newFilm.getAvailable());
        item.setOwnerId(newFilm.getOwnerId() == null ? oldFilm.getOwnerId() : newFilm.getOwnerId());
        item.setRequestId(newFilm.getRequestId() == null ? oldFilm.getRequestId() : newFilm.getRequestId());
        return item;
    }
}

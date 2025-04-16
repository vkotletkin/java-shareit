package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto save(ItemRequestDto itemRequestDto);

    List<ItemRequestDto> findItemsOfUser(long userId);

    List<ItemRequestDto> findItemsNotUser(long userId);

    ItemRequestDto findItemRequestById(long id);
}

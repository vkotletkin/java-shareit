package ru.practicum.shareit.request;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemRequestMapper {

    public static ItemRequest mapToModel(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestor(user)
                .created(itemRequestDto.getCreated())
                .build();
    }

    public static ItemRequestDto mapToDto(ItemRequest itemRequest, List<ItemDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor().getId())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

    public static List<ItemRequestDto> mapToDto(Map<Long, ItemRequest> itemRequests, Map<Long, List<ItemDto>> items) {
        List<ItemRequestDto> itemRequestDtos = new ArrayList<>();
        for (Map.Entry<Long, ItemRequest> itemRequestEntry : itemRequests.entrySet()) {
            ItemRequest itemRequest = itemRequestEntry.getValue();
            ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                    .id(itemRequest.getId())
                    .description(itemRequest.getDescription())
                    .requestor(itemRequest.getRequestor().getId())
                    .created(itemRequest.getCreated())
                    .items(items.get(itemRequest.getId()))
                    .build();
            itemRequestDtos.add(itemRequestDto);
        }
        return itemRequestDtos;
    }
}

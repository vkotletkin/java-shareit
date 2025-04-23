package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemRequestMapperTests {

    @Test
    void mapToModel_shouldMapCorrectly() {
        User user = new User();
        user.setId(1L);

        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("Need item")
                .requestor(1L)
                .created(LocalDateTime.now())
                .build();

        ItemRequest result = ItemRequestMapper.mapToModel(dto, user);

        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(user, result.getRequestor());
        assertEquals(dto.getCreated(), result.getCreated());
    }

    @Test
    void mapToDto_shouldMapCorrectly() {
        User user = new User();
        user.setId(1L);

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Need item")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        List<ItemDto> items = List.of(ItemDto.builder().build());

        ItemRequestDto result = ItemRequestMapper.mapToDto(request, items);

        assertEquals(request.getId(), result.getId());
        assertEquals(request.getDescription(), result.getDescription());
        assertEquals(user.getId(), result.getRequestor());
        assertEquals(request.getCreated(), result.getCreated());
        assertEquals(items, result.getItems());
    }

    @Test
    void mapToDto_withMap_shouldMapCorrectly() {
        User user = new User();
        user.setId(1L);

        ItemRequest request = ItemRequest.builder()
                .id(1L)
                .description("Need item")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        Map<Long, ItemRequest> requests = Map.of(1L, request);
        Map<Long, List<ItemDto>> items = Map.of(1L, List.of(ItemDto.builder().build()));

        List<ItemRequestDto> result = ItemRequestMapper.mapToDto(requests, items);

        assertEquals(1, result.size());
        assertEquals(request.getId(), result.get(0).getId());
        assertEquals(request.getDescription(), result.get(0).getDescription());
        assertEquals(user.getId(), result.get(0).getRequestor());
        assertEquals(request.getCreated(), result.get(0).getCreated());
        assertEquals(items.get(1L), result.get(0).getItems());
    }

    @Test
    void mapToDto_withEmptyMaps_shouldReturnEmptyList() {
        List<ItemRequestDto> result = ItemRequestMapper.mapToDto(Map.of(), Map.of());
        assertTrue(result.isEmpty());
    }
}

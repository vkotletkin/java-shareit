package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemEnrichedDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemControllerUnitTests {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    @Test
    void create_shouldCallServiceAndReturnItemDto() {

        ItemDto inputDto = ItemDto.builder()
                .id(1L)
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .ownerId(1L)
                .build();

        ItemDto outputDto = ItemDto.builder()
                .id(1L)
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .ownerId(1L)
                .build();

        when(itemService.create(any(ItemDto.class))).thenReturn(outputDto);

        ItemDto result = itemController.create(inputDto, 1L);

        assertEquals(outputDto, result);
        verify(itemService).create(any(ItemDto.class));
        assertEquals(1L, inputDto.getOwnerId());
    }

    @Test
    void findAllOnUser_shouldReturnListOfItems() {

        when(itemService.findAllItemsByUser(anyLong())).thenReturn(Collections.emptyList());

        List<ItemDto> result = (List<ItemDto>) itemController.findAllOnUser(1L);

        assertTrue(result.isEmpty());
        verify(itemService).findAllItemsByUser(1L);
    }

    @Test
    void findByIdShouldReturnEnrichedItem() {

        ItemEnrichedDto enrichedDto = ItemEnrichedDto.builder()
                .id(1L)
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .ownerId(1L)
                .lastBooking(LocalDateTime.of(1980, 1, 1, 0, 0, 0))
                .nextBooking(LocalDateTime.of(1980, 1, 2, 0, 0, 0))
                .comments(List.of("Test item comment"))
                .build();

        when(itemService.findById(anyLong())).thenReturn(enrichedDto);

        ItemEnrichedDto result = itemController.findById(1L);

        assertEquals(enrichedDto, result);
        verify(itemService).findById(1L);
    }

    @Test
    void updateShouldCallServiceAndReturnUpdatedItem() {

        ItemDto inputDto = ItemDto.builder()
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .ownerId(1L)
                .build();

        ItemDto outputDto = ItemDto.builder()
                .id(1L)
                .name("Test item name new name")
                .description("Test item description")
                .available(true)
                .ownerId(1L)
                .build();

        when(itemService.update(any(ItemDto.class))).thenReturn(outputDto);

        ItemDto result = itemController.update(inputDto, 1L, 1L);

        assertEquals(outputDto, result);
        verify(itemService).update(any(ItemDto.class));
        assertEquals(1L, inputDto.getId());
        assertEquals(1L, inputDto.getOwnerId());
    }

    @Test
    void searchShouldReturnListOfItems() {

        when(itemService.findByText(anyString())).thenReturn(Collections.emptyList());

        List<ItemDto> result = (List<ItemDto>) itemController.search("test");

        assertTrue(result.isEmpty());
        verify(itemService).findByText("test");
    }
}

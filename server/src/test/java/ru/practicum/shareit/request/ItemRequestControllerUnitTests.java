package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestControllerUnitTests {

    @Mock
    private ItemRequestService service;

    @InjectMocks
    private ItemRequestController controller;

    @Test
    void saveShouldCallServiceAndReturnResult() {

        long userId = 1L;
        ItemRequestDto inputDto = ItemRequestDto.builder().build();
        inputDto.setDescription("Need item");

        ItemRequestDto expectedDto = ItemRequestDto.builder().build();
        expectedDto.setId(1L);
        expectedDto.setDescription("Need item");
        expectedDto.setRequestor(userId);
        expectedDto.setCreated(LocalDateTime.now());

        when(service.save(inputDto)).thenReturn(expectedDto);

        ItemRequestDto result = controller.save(inputDto, userId);

        assertEquals(userId, inputDto.getRequestor());
        assertNotNull(inputDto.getCreated());
        assertEquals(expectedDto, result);
        verify(service).save(inputDto);
    }

    @Test
    void findAllShouldCallServiceAndReturnResult() {

        long userId = 1L;
        List<ItemRequestDto> expected = List.of(ItemRequestDto.builder().build());

        when(service.findItemsOfUser(userId)).thenReturn(expected);

        List<ItemRequestDto> result = controller.findAll(userId);

        assertEquals(expected, result);
        verify(service).findItemsOfUser(userId);
    }

    @Test
    void findAllNotUserShouldCallServiceAndReturnResult() {

        long userId = 1L;
        List<ItemRequestDto> expected = List.of(ItemRequestDto.builder().build());

        when(service.findItemsNotUser(userId)).thenReturn(expected);

        List<ItemRequestDto> result = controller.findAllNotUser(userId);

        assertEquals(expected, result);
        verify(service).findItemsNotUser(userId);
    }

    @Test
    void findByIdShouldCallServiceAndReturnResult() {

        long requestId = 1L;
        ItemRequestDto expected = ItemRequestDto.builder().build();

        when(service.findItemRequestById(requestId)).thenReturn(expected);

        ItemRequestDto result = controller.findById(requestId);

        assertEquals(expected, result);
        verify(service).findItemRequestById(requestId);
    }
}

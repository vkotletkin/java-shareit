package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestServiceUnitTests {


    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private Item createTestItem(Long id, String name, String description, Boolean available, Long ownerId, Long requestId) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setDescription(description);
        item.setAvailable(available);
        item.setOwner(new User(ownerId, "owner", "owner@email.com"));
        if (requestId != null) {
            ItemRequest request = new ItemRequest();
            request.setId(requestId);
            item.setRequestId(requestId);
        }
        return item;
    }

    @Test
    void findItemsOfUserShouldReturnEmptyListWhenNoRequestsFound() {
        long userId = 1L;

        when(itemRequestRepository.findByRequestor_Id(userId))
                .thenReturn(Collections.emptyList());
        when(itemRepository.findAllByOwnerIdNotEquals(userId))
                .thenReturn(Collections.emptyList());

        List<ItemRequestDto> result = itemRequestService.findItemsOfUser(userId);

        assertTrue(result.isEmpty());
        verify(itemRequestRepository).findByRequestor_Id(userId);
        verify(itemRepository).findAllByOwnerIdNotEquals(userId);
    }

    @Test
    void findItemsOfUserShouldReturnRequestsWithItems() {

        long userId = 1L;
        long requestId = 1L;

        User user = new User(userId, "user", "user@email.com");

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setRequestor(user);

        Item item = createTestItem(1L, "item", "description", true, 2L, requestId);

        List<ItemRequest> requests = new ArrayList<>();
        requests.add(request);

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRequestRepository.findByRequestor_Id(userId))
                .thenReturn(requests);
        when(itemRepository.findAllByOwnerIdNotEquals(userId))
                .thenReturn(items);

        List<ItemRequestDto> result = itemRequestService.findItemsOfUser(userId);

        assertEquals(1, result.size());
        assertEquals(requestId, result.getFirst().getId());
        assertEquals(1, result.getFirst().getItems().size());
        assertEquals(item.getId(), result.getFirst().getItems().getFirst().getId());
    }

    @Test
    void findItemsOfUserShouldFilterItemsWithNullRequestId() {
        long userId = 1L;
        long requestId = 1L;

        User user = new User(userId, "user", "user@email.com");

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setRequestor(user);

        Item itemWithRequest = createTestItem(1L, "item1", "description1", true, 2L, requestId);
        Item itemWithoutRequest = createTestItem(2L, "item2", "description2", true, 2L, null);

        List<ItemRequest> requests = new ArrayList<>();
        requests.add(request);

        List<Item> allItems = new ArrayList<>();
        allItems.add(itemWithRequest);
        allItems.add(itemWithoutRequest);

        when(itemRequestRepository.findByRequestor_Id(userId))
                .thenReturn(requests);
        when(itemRepository.findAllByOwnerIdNotEquals(userId))
                .thenReturn(allItems);

        List<ItemRequestDto> result = itemRequestService.findItemsOfUser(userId);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getItems().size());
        assertEquals(itemWithRequest.getId(), result.get(0).getItems().get(0).getId());
    }

    @Test
    void findItemsNotUserShouldReturnEmptyListWhenNoRequestsFound() {
        long userId = 1L;

        when(itemRequestRepository.findByRequestorIdNotEqual(userId))
                .thenReturn(Collections.emptyList());
        when(itemRepository.findAllByOwnerIdNotEquals(userId))
                .thenReturn(Collections.emptyList());

        List<ItemRequestDto> result = itemRequestService.findItemsNotUser(userId);

        assertTrue(result.isEmpty());
        verify(itemRequestRepository).findByRequestorIdNotEqual(userId);
        verify(itemRepository).findAllByOwnerIdNotEquals(userId);
    }

    @Test
    void findItemsNotUserShouldReturnRequestsWithItems() {
        long userId = 1L;
        long requestId = 1L;
        long otherUserId = 2L;

        User otherUser = new User(otherUserId, "other", "other@email.com");

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setRequestor(otherUser);

        Item item = createTestItem(1L, "item", "description", true, 3L, requestId);

        List<ItemRequest> requests = new ArrayList<>();
        requests.add(request);

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRequestRepository.findByRequestorIdNotEqual(userId))
                .thenReturn(requests);
        when(itemRepository.findAllByOwnerIdNotEquals(userId))
                .thenReturn(items);

        List<ItemRequestDto> result = itemRequestService.findItemsNotUser(userId);

        assertEquals(1, result.size());
        assertEquals(requestId, result.get(0).getId());
        assertEquals(1, result.get(0).getItems().size());
        assertEquals(item.getId(), result.get(0).getItems().get(0).getId());
    }

    @Test
    void findItemsNotUserShouldIncludeRequestsWithoutItems() {
        long userId = 1L;
        long requestId = 1L;
        long otherUserId = 2L;

        User otherUser = new User(otherUserId, "other", "other@email.com");

        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        request.setRequestor(otherUser);

        List<ItemRequest> requests = new ArrayList<>();
        requests.add(request);

        when(itemRequestRepository.findByRequestorIdNotEqual(userId))
                .thenReturn(requests);
        when(itemRepository.findAllByOwnerIdNotEquals(userId))
                .thenReturn(Collections.emptyList());

        List<ItemRequestDto> result = itemRequestService.findItemsNotUser(userId);

        assertEquals(1, result.size());
        assertEquals(requestId, result.get(0).getId());
    }
}

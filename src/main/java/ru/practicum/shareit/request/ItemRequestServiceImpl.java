package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dal.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь с идентификатором {0} не найден";
    private static final String ITEM_REQUEST_NOT_FOUND_MESSAGE = "Запрос с идентификатором {0} не найден";

    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto save(ItemRequestDto itemRequestDto) {
        User user = userRepository.findById(itemRequestDto.getRequestor())
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, itemRequestDto.getRequestor()));
        ItemRequest itemRequest = ItemRequestMapper.mapToModel(itemRequestDto, user);
        itemRequest = itemRequestRepository.save(itemRequest);
        List<ItemDto> items = ItemMapper.mapToDto(itemRepository.findAllByRequestId(itemRequest.getId()));
        return ItemRequestMapper.mapToDto(itemRequest, items);
    }

    @Override
    public List<ItemRequestDto> findItemsOfUser(long userId) {
        // Получили все объекты, где предметы не принадлежат юзеру
        Map<Long, ItemRequest> itemRequests = itemRequestRepository.findByRequestor_Id(userId)
                .stream().collect(Collectors.toMap(ItemRequest::getId, obj -> obj));

        // получили все предметы, которые не принадлежат юзеру
        Map<Long, List<ItemDto>> items = ItemMapper.mapToDto(itemRepository.findAllByOwnerIdNotEquals(userId))
                .stream()
                .filter(obj -> obj.getRequestId() != null)
                .collect(Collectors.groupingBy(ItemDto::getRequestId));

        return ItemRequestMapper.mapToDto(itemRequests, items);
    }

    @Override
    public List<ItemRequestDto> findItemsNotUser(long userId) {
        // Получили все объекты, где предметы не принадлежат юзеру
        Map<Long, ItemRequest> itemRequests = itemRequestRepository.findByRequestorIdNotEqual(userId)
                .stream().collect(Collectors.toMap(ItemRequest::getId, obj -> obj));
        // получили все предметы, которые не принадлежат юзеру
        Map<Long, List<ItemDto>> items = ItemMapper.mapToDto(itemRepository.findAllByOwnerIdNotEquals(userId))
                .stream().collect(Collectors.groupingBy(ItemDto::getRequestId));

        return ItemRequestMapper.mapToDto(itemRequests, items);
    }

    @Override
    public ItemRequestDto findItemRequestById(long id) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(notFoundException(ITEM_REQUEST_NOT_FOUND_MESSAGE, id));
        List<ItemDto> items = ItemMapper.mapToDto(itemRepository.findAllByRequestId(itemRequest.getId()));
        return ItemRequestMapper.mapToDto(itemRequest, items);
    }
}
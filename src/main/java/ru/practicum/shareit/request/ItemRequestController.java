package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";

    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto save(@RequestBody ItemRequestDto itemRequestDto,
                               @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {

        itemRequestDto.setRequestor(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        return service.save(itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> findAll(@RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return service.findItemsOfUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> findAllNotUser(@RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return service.findItemsNotUser(userId);
    }

    @GetMapping("/{request-id}")
    public ItemRequestDto findById(@PathVariable(name = "request-id") Long requestId) {
        return service.findItemRequestById(requestId);
    }
}

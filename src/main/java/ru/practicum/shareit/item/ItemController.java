package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";
    private static final String ENDPOINT_PATH_ID = "/{id}";

    private final ItemService service;

    @PostMapping
    public ItemDto create(@Valid @RequestBody final ItemDto itemDto,
                          @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        itemDto.setOwnerId(userId);
        return service.create(itemDto);
    }

    @GetMapping
    public Collection<ItemDto> findAllOnUser(@RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return service.findAllOnUser(userId);
    }

    @GetMapping(ENDPOINT_PATH_ID)
    public ItemDto findById(@PathVariable(name = "id") final Long itemId) {
        return service.findById(itemId);
    }

    @PatchMapping(ENDPOINT_PATH_ID)
    public ItemDto update(@RequestBody final ItemDto itemDto,
                          @PathVariable(name = "id") final Long itemId,
                          @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        itemDto.setId(itemId);
        itemDto.setOwnerId(userId);
        return service.update(itemDto);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestParam(name = "text") String text,
                                      @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return service.findByText(text, userId);
    }
}

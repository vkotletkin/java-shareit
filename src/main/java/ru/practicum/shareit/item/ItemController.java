package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemDto> getItems() {
        return List.of();
    }

    @GetMapping("/{item-id}")
    public ItemDto getItem(@PathVariable(name = "item-id") final Long itemId) {
        return new ItemDto();
    }

    @PostMapping
    public ItemDto insertItem(@RequestBody final ItemDto itemDto,
                              @RequestHeader(USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return itemDto;
    }

    @PatchMapping
    public ItemDto patchItem(@RequestBody final ItemDto itemDto,
                             @RequestHeader(USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return itemDto;
    }
}

package ru.practicum.shareit.items;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.items.dto.CommentDto;
import ru.practicum.shareit.items.dto.ItemDto;

@Validated
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";
    private static final String ENDPOINT_PATH_ID = "/{id}";

    private final ItemClient client;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody final ItemDto itemDto,
                                         @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.create(itemDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAllOnUser(@RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.findAllItemsByUser(userId);
    }

    @GetMapping(ENDPOINT_PATH_ID)
    public ResponseEntity<Object> findById(@PathVariable(name = "id") final Long itemId) {
        return client.findById(itemId);
    }

    @PatchMapping(ENDPOINT_PATH_ID)
    public ResponseEntity<Object> update(@RequestBody final ItemDto itemDto,
                                         @PathVariable(name = "id") final Long itemId,
                                         @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.update(itemDto, itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam(name = "text") String text,
                                         @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.findByText(text, userId);
    }

    @PostMapping(ENDPOINT_PATH_ID + "/comment")
    public ResponseEntity<Object> createComment(@RequestBody CommentDto commentDto,
                                                @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId,
                                                @PathVariable(name = "id") Long itemId) {
        return client.createComment(commentDto, userId, itemId);
    }
}

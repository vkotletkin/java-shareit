package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";

    private final ItemRequestClient client;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody ItemRequestDto itemRequestDto,
                                       @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.save(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.findItemsOfUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllNotUser(@RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.findItemsNotUser(userId);
    }

    @GetMapping("/{request-id}")
    public ResponseEntity<Object> findById(@PathVariable(name = "request-id") Long requestId,
                                           @RequestHeader(name = USER_IDENTIFICATOR_HEADER_NAME) Long userId) {
        return client.findItemRequestById(requestId, userId);
    }
}
package ru.practicum.shareit.users;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.dto.BaseResponse;

@Validated
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final String ENDPOINT_PATH_ID = "/{id}";

    private final UserClient client;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody final UserDto userDto) {
        return client.create(userDto);
    }

    @GetMapping(ENDPOINT_PATH_ID)
    public ResponseEntity<Object> findById(@PathVariable long id) {
        return client.findById(id);
    }

    @PatchMapping(ENDPOINT_PATH_ID)
    public ResponseEntity<Object> update(@RequestBody UserDto userDto, @PathVariable Long id) {
        return client.update(userDto, id);
    }

    @DeleteMapping(ENDPOINT_PATH_ID)
    public ResponseEntity<Object> delete(@PathVariable long id) {
        return client.delete(id);
    }
}
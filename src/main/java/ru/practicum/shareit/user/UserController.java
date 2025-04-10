package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.dto.BaseResponse;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private static final String ENDPOINT_PATH_ID = "/{id}";

    private final UserService service;

    @PostMapping
    public UserDto create(@Valid @RequestBody final UserDto userDto) {
        return service.create(userDto);
    }

    @GetMapping(ENDPOINT_PATH_ID)
    public UserDto findById(@PathVariable long id) {
        return service.findById(id);
    }

    @PatchMapping(ENDPOINT_PATH_ID)
    public UserDto update(@RequestBody UserDto userDto, @PathVariable Long id) {
        userDto.setId(id);
        return service.update(userDto);
    }

    @DeleteMapping(ENDPOINT_PATH_ID)
    public BaseResponse delete(@PathVariable long id) {
        return service.delete(id);
    }
}

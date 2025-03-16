package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final String ENDPOINT_PATH_ID = "/{id}";
    private final UserService service;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @GetMapping(ENDPOINT_PATH_ID)
    public UserDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @DeleteMapping(ENDPOINT_PATH_ID)
    public void delete(@PathVariable long id) {
        service.delete(id);
    }

    @PatchMapping(ENDPOINT_PATH_ID)
    public UserDto update(@Valid @RequestBody UserDto userDto, @PathVariable Long id) {
        return service.update(userDto);
    }
}

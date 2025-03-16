package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto findById(long id);

    void delete(long id);
}

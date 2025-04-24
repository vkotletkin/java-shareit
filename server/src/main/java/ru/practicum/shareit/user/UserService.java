package ru.practicum.shareit.user;

import ru.practicum.shareit.common.dto.BaseResponse;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto findById(long id);

    BaseResponse delete(long id);
}

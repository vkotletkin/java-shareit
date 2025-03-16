package ru.practicum.shareit.user;

import ru.practicum.shareit.base.BaseResponse;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto);

    UserDto findById(long id);

    BaseResponse delete(long id);
}

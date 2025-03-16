package ru.practicum.shareit.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.base.BaseResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final static String USER_NOT_FOUND_MESSAGE = "Пользователь с идентификатором {0} не найден";

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToModel(userDto);
        checkExistEmail(user.getEmail());
        user = userRepository.create(user);
        return UserMapper.mapToDto(user);
    }

    @Override
    public UserDto update(UserDto userDto) {
        User user = UserMapper.mapToModel(userDto);
        User oldUser = userRepository.findById(user.getId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        User result = update(oldUser, user);
        userRepository.update(result);
        return UserMapper.mapToDto(result);
    }

    @Override
    public UserDto findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
        return UserMapper.mapToDto(user);
    }

    @Override
    public BaseResponse delete(long id) {
        userRepository.delete(id);
        return new BaseResponse(String.format("Пользователь с идентификатором %d - удален.", id));
    }

    private User update(User originalUser, User newUser) {
        return User.builder()
                .id(newUser.getId())
                .email(newUser.getEmail() == null ? originalUser.getEmail() : newUser.getEmail())
                .name(newUser.getName() == null ? originalUser.getName() : newUser.getName())
                .build();
    }

    private void checkExistUser(long id) {
        userRepository.findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
    }

    private void checkExistEmail(String email) {
        userRepository.checkEmailExists(email);
    }
}

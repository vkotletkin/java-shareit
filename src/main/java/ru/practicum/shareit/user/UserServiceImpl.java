package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.dto.BaseResponse;
import ru.practicum.shareit.exception.AlreadyExistsEmailException;

import static ru.practicum.shareit.exception.NotFoundException.notFoundException;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND_MESSAGE = "Пользователь с идентификатором {0} не найден";
    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "Электронная почта - {0} уже существует.";

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToModel(userDto);
        checkExistEmail(user);
        user = userRepository.save(user);
        return UserMapper.mapToDto(user);
    }

    @Override
    @Transactional
    public UserDto update(UserDto userDto) {
        User user = UserMapper.mapToModel(userDto);
        User oldUser = userRepository.findById(user.getId()).orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE));
        User result = update(oldUser, user);
        checkExistEmail(result);
        userRepository.save(result);
        return UserMapper.mapToDto(result);
    }

    @Override
    public UserDto findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(notFoundException(USER_NOT_FOUND_MESSAGE, id));
        return UserMapper.mapToDto(user);
    }

    @Override
    @Transactional
    public BaseResponse delete(long id) {
        userRepository.deleteById(id);
        return new BaseResponse(String.format("Пользователь с идентификатором %d - удален.", id));
    }

    private void checkExistEmail(User user) {
        long count;
        if (user.getId() == null) {
            count = userRepository.countFindByEmail(user.getEmail());
        } else {
            count = userRepository.countFindByEmailAndIdIsNot(user.getEmail(), user.getId());
        }

        if (count != 0L) {
            throw new AlreadyExistsEmailException(EMAIL_EXISTS_ERROR_MESSAGE, user.getEmail());
        }
    }

    private User update(User originalUser, User newUser) {
        User user = new User();
        user.setId(newUser.getId());
        user.setEmail(newUser.getEmail() == null ? originalUser.getEmail() : newUser.getEmail());
        user.setName(newUser.getName() == null ? originalUser.getName() : newUser.getName());
        return user;
    }
}

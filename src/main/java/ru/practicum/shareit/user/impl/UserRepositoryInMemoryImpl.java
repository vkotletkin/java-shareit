package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.AlreadyExistsEmailException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryInMemoryImpl implements UserRepository {

    private static final String EMAIL_EXISTS_ERROR_MESSAGE = "Email: {0} уже существует.";

    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User create(User user) {
        Long id = getNextId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public void checkEmailExists(String email) {
        long count = users.values().stream().map(User::getEmail).filter(email::equals).count();
        if (count != 0L) {
            throw new AlreadyExistsEmailException(EMAIL_EXISTS_ERROR_MESSAGE, email);
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

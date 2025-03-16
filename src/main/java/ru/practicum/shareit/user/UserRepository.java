package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {
    User create(User user);

    void update(User user);

    Optional<User> findById(long id);

    void delete(long id);

    void checkEmailExists(User user);
}

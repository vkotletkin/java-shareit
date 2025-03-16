package ru.practicum.shareit.user;

import java.util.Optional;

public interface UserRepository {
    User create(User user);

    User update(User user);

    Optional<User> findById(long id);

    void delete(long id);

    void checkEmailExists(String email);
}

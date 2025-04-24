package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(long id);

    long countFindByEmailAndIdIsNot(String email, long id);

    long countFindByEmail(String email);
}

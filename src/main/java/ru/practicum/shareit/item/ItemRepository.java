package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findByOwnerIdEquals(User ownerId);

    @Query("""
            SELECT it FROM Item it
            WHERE (LOWER(it.name) LIKE LOWER(?1) OR LOWER(it.description) LIKE LOWER(?1)) AND it.available = true
            """)
    Collection<Item> findTextNameAndDescription(String text);
}

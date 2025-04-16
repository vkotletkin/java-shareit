package ru.practicum.shareit.item.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Collection<Item> findByOwnerIdEquals(long ownerId);

    @Query("""
            SELECT it FROM Item it
            WHERE (LOWER(it.name) LIKE LOWER(?1) OR LOWER(it.description) LIKE LOWER(?1)) AND it.available = true
            """)
    Collection<Item> findTextNameAndDescription(String text);


    long countItemsByOwnerIdEquals(long userId);

    List<Item> findAllByRequestId(long requestId);

    @Query("""
            SELECT it FROM Item it
            WHERE it.owner.id != ?1
            """)
    List<Item> findAllByOwnerIdNotEquals(long ownerId);
}

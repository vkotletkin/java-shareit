package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemRepository {
    Item create(Item item);

    void update(Item item);

    Optional<Item> findById(long id);

    Collection<Item> findAllByUser(long id);

    Collection<Item> findByText(String text);
}

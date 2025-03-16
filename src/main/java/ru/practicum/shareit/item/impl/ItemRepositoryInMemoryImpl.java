package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryInMemoryImpl implements ItemRepository {

    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        Long id = getNextId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public void update(Item item) {
        long id = item.getId();
        if (Objects.equals(items.get(id).getOwnerId(), item.getOwnerId())) {
            items.put(item.getId(), item);
        } else {
            throw new IncorrectOwnerException("Для изменения передан идентификатор, не соответствующий владельцу.");
        }
    }

    @Override
    public Optional<Item> findById(long id) {
        return Optional.ofNullable(items.get(id));
    }

    @Override
    public Collection<Item> findAllByUser(long userId) {
        return items.values().stream().filter(u -> u.getOwnerId() == userId).collect(Collectors.toList());
    }

    @Override
    public Collection<Item> findByText(String text) {
        return items.values().stream()
                .filter(u -> (u.getName().toLowerCase().contains(text.toLowerCase())
                        || u.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && u.isAvailable())
                .collect(Collectors.toList());
    }

    private long getNextId() {
        long currentMaxId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}

package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemTests {

    @Test
    void item_shouldHaveCorrectGettersAndSetters() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");
        item.setDescription("Test Description");
        item.setAvailable(true);

        User owner = new User();
        owner.setId(1L);
        item.setOwner(owner);

        assertEquals(1L, item.getId());
        assertEquals("Test Item", item.getName());
        assertEquals("Test Description", item.getDescription());
        assertTrue(item.getAvailable());
        assertEquals(owner, item.getOwner());
    }
}

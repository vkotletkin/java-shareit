package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemDtoJsonTest {

    private final JacksonTester<Item> json;

    @Test
    public void testItemDto() {
        ItemDto itemDto = ItemDto.builder()
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .build();
    }
}

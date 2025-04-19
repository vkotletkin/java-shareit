package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemDtoJsonTest {

    private final JacksonTester<ItemDto> json;

    @Test
    public void testItemDto() throws Exception {
        ItemDto itemDto = ItemDto.builder()
                .id(1L)
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .ownerId(1L)
                .build();

        JsonContent<ItemDto> jsonContent = json.write(itemDto);

        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonContent).extractingJsonPathStringValue("$.name").isEqualTo("Test item name");
        assertThat(jsonContent).extractingJsonPathStringValue("$.description")
                .isEqualTo("Test item description");
        assertThat(jsonContent).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(jsonContent).extractingJsonPathNumberValue("$.ownerId").isEqualTo(1);
    }
}

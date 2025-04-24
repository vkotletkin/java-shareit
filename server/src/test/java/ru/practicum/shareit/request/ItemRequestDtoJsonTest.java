package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemRequestDtoJsonTest {

    private final JacksonTester<ItemRequestDto> json;

    @Test
    public void testItemRequestDto() throws Exception {
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Simple Request Description")
                .requestor(1L)
                .build();

        JsonContent<ItemRequestDto> jsonResponse = json.write(itemRequestDto);

        assertThat(jsonResponse).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(jsonResponse).extractingJsonPathStringValue("$.description")
                .isEqualTo(itemRequestDto.getDescription());
        assertThat(jsonResponse).extractingJsonPathNumberValue("$.requestor").isEqualTo(1);
    }
}

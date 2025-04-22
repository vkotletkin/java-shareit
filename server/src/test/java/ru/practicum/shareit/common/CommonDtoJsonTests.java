package ru.practicum.shareit.common;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.common.dto.BaseResponse;
import ru.practicum.shareit.common.dto.CatalogDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommonDtoJsonTests {

    private final JacksonTester<BaseResponse> jsonBaseResponse;
    private final JacksonTester<CatalogDto> jsonCatalog;

    @Test
    public void testJsonBaseResponse() throws Exception {
        BaseResponse baseResponse = new BaseResponse("User is deleted");

        JsonContent<BaseResponse> result = jsonBaseResponse.write(baseResponse);

        assertThat(result).extractingJsonPathStringValue("$.message").isEqualTo(baseResponse.message());
    }

    @Test
    public void testJsonCatalog() throws Exception {
        CatalogDto catalogDto = CatalogDto.builder()
                .id(1L)
                .name("Test")
                .build();

        JsonContent<CatalogDto> result = jsonCatalog.write(catalogDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Test");
    }
}

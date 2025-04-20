package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemEnrichedDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
public class ItemControllerTests {

    private static final String ENDPOINT_PATH_ID = "/{id}";
    public static String ITEMS_ENDPOINT = "/items";
    public static String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";

    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("Test item name")
            .description("Test item description")
            .available(true)
            .ownerId(1L)
            .build();

    private final ItemEnrichedDto itemEnrichedDto = ItemEnrichedDto.builder()
            .id(1L)
            .name("Test item name")
            .description("Test item description")
            .available(true)
            .ownerId(1L)
            .lastBooking(LocalDateTime.of(1980, 1, 1, 0, 0, 0))
            .nextBooking(LocalDateTime.of(1980, 1, 2, 0, 0, 0))
            .comments(List.of("Test item comment"))
            .build();

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void testItemCreate() throws Exception {
        when(itemService.create(any())).thenReturn(itemDto);

        mvc.perform(post(ITEMS_ENDPOINT)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mvc.perform(post(ITEMS_ENDPOINT)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_IDENTIFICATOR_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemDto.getOwnerId().intValue())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    public void testFindAllOnUser() throws Exception {
        when(itemService.findAllItemsByUser(anyLong())).thenReturn(List.of(itemDto));

        mvc.perform(get(ITEMS_ENDPOINT)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mvc.perform(get(ITEMS_ENDPOINT)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_IDENTIFICATOR_HEADER_NAME, 1))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].ownerId", is(itemDto.getOwnerId().intValue())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())));
    }

    @Test
    public void testFindById() throws Exception {
        when(itemService.findById(anyLong())).thenReturn(itemEnrichedDto);

        mvc.perform(get(ITEMS_ENDPOINT + ENDPOINT_PATH_ID, itemEnrichedDto.getId())
                        .content(mapper.writeValueAsString(itemEnrichedDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_IDENTIFICATOR_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemEnrichedDto.getName())))
                .andExpect(jsonPath("$.available", is(itemEnrichedDto.getAvailable())))
                .andExpect(jsonPath("$.ownerId", is(itemEnrichedDto.getOwnerId().intValue())))
                .andExpect(jsonPath("$.description", is(itemEnrichedDto.getDescription())))
                .andExpect(jsonPath("$.comments", is(itemEnrichedDto.getComments())))
                .andExpect(jsonPath("$.nextBooking").exists())
                .andExpect(jsonPath("$.lastBooking").exists());
    }
}

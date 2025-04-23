package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Rollback
@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceIntegrationTests {

    private final UserRepository userRepository;
    private final ItemService itemService;

    private User user;
    private ItemDto itemDto;

    @BeforeEach
    public void setUp() {

        user = User.builder()
                .name("Vladislav")
                .email("vkotletkin@gmail.com")
                .build();

        itemDto = ItemDto.builder()
                .name("Test item name")
                .description("Test item description")
                .available(true)
                .build();
    }

    @Test
    public void testSaveItem() {

        user = userRepository.save(user);

        itemDto.setOwnerId(user.getId());

        ItemDto itemDtoNew = itemService.create(itemDto);

        assertThat(itemDtoNew.getId(), notNullValue());
        assertThat(itemDtoNew.getName(), equalTo(itemDto.getName()));
        assertThat(itemDtoNew.getDescription(), equalTo(itemDto.getDescription()));
        assertThat(itemDtoNew.getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(itemDtoNew.getOwnerId(), equalTo(itemDto.getOwnerId()));
    }

    @Test
    public void testFindAllItemsForUser() {

        user = userRepository.save(user);

        itemDto.setOwnerId(user.getId());

        ItemDto itemDtoNew = itemService.create(itemDto);

        Collection<ItemDto> itemDtoList = itemService.findAllItemsByUser(user.getId());

        assertThat(itemDtoNew.getId(), notNullValue());
        assertThat(itemDtoList.size(), equalTo(1));
    }
}

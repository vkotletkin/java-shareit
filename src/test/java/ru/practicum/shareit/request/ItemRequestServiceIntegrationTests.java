package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Rollback
@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestServiceIntegrationTests {

    private final ItemRequestService service;
    private final UserRepository userRepository;

    private ItemRequestDto itemRequestDto;
    private User userBase;

    @BeforeEach
    public void setUp() {
        userBase = User.builder()
                .name("Vladislav")
                .email("vkotletkin@gmail.com")
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .description("Simple Request Description")
                .created(LocalDateTime.now())
                .requestor(userBase.getId())
                .build();

    }

    @Test
    public void testSaveNewUser() {

        User user = userRepository.save(userBase);
        itemRequestDto.setRequestor(user.getId());

        ItemRequestDto itemRequestDtoNew = service.save(itemRequestDto);

        assertThat(itemRequestDtoNew.getId(), notNullValue());
        assertThat(itemRequestDtoNew.getDescription(), equalTo(itemRequestDto.getDescription()));
        assertThat(itemRequestDtoNew.getCreated(), equalTo(itemRequestDto.getCreated()));
        assertThat(itemRequestDtoNew.getRequestor(), equalTo(itemRequestDto.getRequestor()));
    }

    @Test
    public void testFindItemRequest() {

        User user = userRepository.save(userBase);
        itemRequestDto.setRequestor(user.getId());

        ItemRequestDto itemRequestDtoSave = service.save(itemRequestDto);
        ItemRequestDto itemRequestDto1 = service.findItemRequestById(itemRequestDtoSave.getId());

        assertThat(itemRequestDto1.getId(), notNullValue());
        assertThat(itemRequestDto1.getDescription(), equalTo(itemRequestDtoSave.getDescription()));
        assertThat(itemRequestDto1.getCreated(), equalTo(itemRequestDtoSave.getCreated()));
        assertThat(itemRequestDto1.getRequestor(), equalTo(itemRequestDtoSave.getRequestor()));
    }
}

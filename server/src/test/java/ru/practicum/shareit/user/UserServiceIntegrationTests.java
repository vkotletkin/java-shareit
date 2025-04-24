package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Rollback
@Transactional
@ActiveProfiles("test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceIntegrationTests {

    private final UserService userService;

    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder()
                .name("Vladislav")
                .email("vladislavkotletkin@gmail.com")
                .build();
    }

    @Test
    public void testSaveUser() {

        UserDto userDtoNew = userService.create(userDto);

        assertThat(userDtoNew.getId(), notNullValue());
        assertThat(userDtoNew.getName(), equalTo(userDto.getName()));
        assertThat(userDtoNew.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    public void testFindUser() {

        userDto = userService.create(userDto);

        UserDto userDtoNew = userService.findById(userDto.getId());

        assertThat(userDtoNew.getId(), notNullValue());
        assertThat(userDtoNew.getName(), equalTo(userDto.getName()));
        assertThat(userDtoNew.getEmail(), equalTo(userDto.getEmail()));
    }
}

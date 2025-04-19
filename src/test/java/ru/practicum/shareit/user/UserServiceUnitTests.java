package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTests {

    @Mock
    private UserService userService;


    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        userDto = UserDto.builder()
                .id(1L)
                .name("Vladislav")
                .email("vladislavkotletkin@gmail.com")
                .build();
    }

    @Test
    public void testFindById() {

        when(userService.findById(userDto.getId()))
                .thenReturn(userDto);

        UserDto userDtoNew = userService.findById(userDto.getId());

        Mockito.verify(userService, Mockito.times(1)).findById(userDto.getId());
        assertThat(userDtoNew.getId(), equalTo(userDto.getId()));
    }
}

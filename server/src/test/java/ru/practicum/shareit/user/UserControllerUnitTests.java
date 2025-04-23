package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.common.dto.BaseResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTests {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void createShouldCallServiceAndReturnDto() {
        UserDto inputDto = UserDto.builder().name("test").email("test@test.com").build();
        UserDto outputDto = UserDto.builder().id(1L).name("test").email("test@test.com").build();

        when(userService.create(inputDto)).thenReturn(outputDto);

        UserDto result = userController.create(inputDto);

        assertEquals(outputDto, result);
        verify(userService).create(inputDto);
    }

    @Test
    void findByIdShouldCallServiceAndReturnDto() {
        long userId = 1L;
        UserDto expectedDto = UserDto.builder().id(userId).name("test").email("test@test.com").build();

        when(userService.findById(userId)).thenReturn(expectedDto);

        UserDto result = userController.findById(userId);

        assertEquals(expectedDto, result);
        verify(userService).findById(userId);
    }

    @Test
    void updateShouldCallServiceAndReturnDto() {
        long userId = 1L;
        UserDto inputDto = UserDto.builder().name("updated").build();
        UserDto outputDto = UserDto.builder().id(userId).name("updated").email("test@test.com").build();

        when(userService.update(inputDto)).thenReturn(outputDto);

        UserDto result = userController.update(inputDto, userId);

        assertEquals(outputDto, result);
        verify(userService).update(inputDto);
    }

    @Test
    void deleteShouldCallServiceAndReturnResponse() {
        long userId = 1L;
        BaseResponse expectedResponse = new BaseResponse("User deleted");

        when(userService.delete(userId)).thenReturn(expectedResponse);

        BaseResponse result = userController.delete(userId);

        assertEquals(expectedResponse, result);
        verify(userService).delete(userId);
    }
}

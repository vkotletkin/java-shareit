package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AlreadyExistsEmailException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @Transactional
    void create_shouldSaveAndReturnUserDto() {
        UserDto inputDto = UserDto.builder().name("test").email("test@test.com").build();
        User savedUser = User.builder().id(1L).name("test").email("test@test.com").build();

        when(userRepository.countFindByEmail("test@test.com")).thenReturn(0L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.create(inputDto);

        assertEquals(1L, result.getId());
        assertEquals("test", result.getName());
        assertEquals("test@test.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @Transactional
    void create_shouldThrowExceptionWhenEmailExists() {
        UserDto inputDto = UserDto.builder().name("test").email("test@test.com").build();

        when(userRepository.countFindByEmail("test@test.com")).thenReturn(1L);

        assertThrows(AlreadyExistsEmailException.class, () -> userService.create(inputDto));
    }

    @Test
    void findById_shouldReturnUserDto() {
        long userId = 1L;
        User user = User.builder().id(userId).name("test").email("test@test.com").build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.findById(userId);

        assertEquals(userId, result.getId());
        assertEquals("test", result.getName());
        assertEquals("test@test.com", result.getEmail());
    }

    @Test
    void findById_shouldThrowNotFoundException() {
        long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    @Transactional
    void delete_shouldCallRepository() {
        long userId = 1L;

        userService.delete(userId);

        verify(userRepository).deleteById(userId);
    }
}

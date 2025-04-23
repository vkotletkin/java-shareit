package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTests {

    @Test
    void mapToModel_shouldMapDtoToModel() {
        UserDto dto = UserDto.builder()
                .id(1L)
                .name("test")
                .email("test@test.com")
                .build();

        User user = UserMapper.mapToModel(dto);

        assertEquals(dto.getId(), user.getId());
        assertEquals(dto.getName(), user.getName());
        assertEquals(dto.getEmail(), user.getEmail());
    }

    @Test
    void mapToDto_shouldMapModelToDto() {
        User user = User.builder()
                .id(1L)
                .name("test")
                .email("test@test.com")
                .build();

        UserDto dto = UserMapper.mapToDto(user);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getName(), dto.getName());
        assertEquals(user.getEmail(), dto.getEmail());
    }
}
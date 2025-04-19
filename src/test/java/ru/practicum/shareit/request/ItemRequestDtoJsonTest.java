package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.UserDto;

@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemRequestDtoJsonTest {

    private final JacksonTester<UserDto> json;
}

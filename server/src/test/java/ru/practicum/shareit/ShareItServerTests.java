package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShareItServerTests {

    @Test
    void contextLoads() {
        // Этот тест проверяет, что контекст Spring загружается корректно
        assertDoesNotThrow(() -> ShareItServer.main(new String[]{}));
    }
}

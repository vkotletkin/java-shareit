package ru.practicum.shareit.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.common.dto.BaseResponse;

@RestController
@RequestMapping("/home")
@Slf4j
public class Controller {

    @GetMapping
    public BaseResponse home() {
        log.info("Hello");
        return new BaseResponse("Hello, World!");
    }
}

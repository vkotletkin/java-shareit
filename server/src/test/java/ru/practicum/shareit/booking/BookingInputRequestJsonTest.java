package ru.practicum.shareit.booking;

import com.jayway.jsonpath.JsonPath;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingInputRequest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingInputRequestJsonTest {

    private final JacksonTester<BookingInputRequest> json;

    @Test
    public void testBookingInputRequest() throws Exception {
        BookingInputRequest bookingInputRequest = BookingInputRequest.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .status(BookingStatus.WAITING)
                .build();

        JsonContent<BookingInputRequest> jsonContent = json.write(bookingInputRequest);

        LocalDateTime start = LocalDateTime.parse(JsonPath.read(jsonContent.getJson(), "$.start"));
        LocalDateTime end = LocalDateTime.parse(JsonPath.read(jsonContent.getJson(), "$.end"));


        assertThat(jsonContent).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(start).isEqualTo(bookingInputRequest.getStart());
        assertThat(end).isEqualTo(bookingInputRequest.getEnd());
        assertThat(bookingInputRequest.getStatus()).isEqualTo(BookingStatus.WAITING);
    }
}

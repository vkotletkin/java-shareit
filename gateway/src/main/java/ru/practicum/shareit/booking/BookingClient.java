package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookingInputRequest;
import ru.practicum.shareit.client.BaseClient;

@Service
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> create(BookingInputRequest bookingInputRequest, long userId) {
        return post("", userId, bookingInputRequest);
    }

    public ResponseEntity<Object> statusChange(long bookingId, boolean approved, long userId) {
        return patch("/" + bookingId + "?approved=" + approved, userId);
    }

    public ResponseEntity<Object> specificBooking(long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookings(BookingState state, long userId) {
        return get("?state=" + state, userId);
    }

    public ResponseEntity<Object> getAllBookingsOfUserItems(BookingState state, long userId) {
        return get("/owner?state=" + state, userId);
    }
}

package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputRequest;
import ru.practicum.shareit.common.dto.CatalogDto;
import ru.practicum.shareit.exception.IncorrectOwnerException;
import ru.practicum.shareit.exception.NotFoundException;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTests {

    public static final String BOOKING_ENDPOINT = "/bookings";
    public static final String USER_IDENTIFICATOR_HEADER_NAME = "X-Sharer-User-Id";

    private final BookingInputRequest bookingInputRequest = BookingInputRequest.builder()
            .id(1L)
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(3))
            .status(BookingStatus.WAITING)
            .itemId(1L)
            .build();

    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(LocalDateTime.now().plusDays(1))
            .end(LocalDateTime.now().plusDays(3))
            .status(BookingStatus.APPROVED)
            .booker(CatalogDto.builder()
                    .id(1L)
                    .name("test name")
                    .build())
            .build();

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    @Test
    public void testBookingCreate() throws Exception {
        when(bookingService.create(any())).thenReturn(bookingDto);

        mvc.perform(post(BOOKING_ENDPOINT)
                        .content(mapper.writeValueAsString(bookingInputRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        mvc.perform(post(BOOKING_ENDPOINT)
                        .content(mapper.writeValueAsString(bookingInputRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(USER_IDENTIFICATOR_HEADER_NAME, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId().intValue())))
                .andExpect(jsonPath("$.start").exists())
                .andExpect(jsonPath("$.end").exists())
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void create_shouldValidateInput() throws Exception {
        // Given - invalid request (missing itemId)

        BookingInputRequest bookingInputRequest = BookingInputRequest.builder()
                .id(1L)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .build();

        // When & Then
        mvc.perform(post("/bookings")
                        .header(USER_IDENTIFICATOR_HEADER_NAME, 500L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(mapper.writeValueAsString(bookingInputRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void statusChange_shouldApproveBooking() throws Exception {
        // Given

        when(bookingService.statusChange(eq(bookingDto.getId()), eq(true), eq(bookingDto.getBooker().getId())))
                .thenReturn(bookingDto);

        // When & Then
        mvc.perform(patch("/bookings/{booking-id}", bookingDto.getId())
                        .header(USER_IDENTIFICATOR_HEADER_NAME, bookingDto.getBooker().getId())
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void get_shouldReturnBooking() throws Exception {
        // Given

        when(bookingService.specificBooking(eq(bookingDto.getId()), eq(bookingDto.getBooker().getId())))
                .thenReturn(bookingDto);

        // When & Then
        mvc.perform(get("/bookings/{booking-id}", bookingDto.getId())
                        .header(USER_IDENTIFICATOR_HEADER_NAME, bookingDto.getBooker().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void getAllBookingsOfUser_shouldReturnList() throws Exception {


        when(bookingService.getAllBookings(eq(BookingState.ALL), eq(bookingDto.getBooker().getId())))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get(BOOKING_ENDPOINT)
                        .header(USER_IDENTIFICATOR_HEADER_NAME, bookingDto.getBooker().getId())
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getAllBookingsOfUser_shouldUseDefaultState() throws Exception {

        when(bookingService.getAllBookings(eq(BookingState.ALL), eq(bookingDto.getBooker().getId())))
                .thenReturn(List.of());

        // When & Then
        mvc.perform(get("/bookings")
                        .header(USER_IDENTIFICATOR_HEADER_NAME, bookingDto.getBooker().getId()))
                .andExpect(status().isOk());
    }


    @Test
    void get_shouldReturnNotFoundForInvalidBookingId() throws Exception {

        Long userId = 1L;

        when(bookingService.specificBooking(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Booking not found"));

        mvc.perform(get("/bookings/{booking-id}", 999L)
                        .header(USER_IDENTIFICATOR_HEADER_NAME, userId))
                .andExpect(status().isNotFound());
    }

    @Test
    void statusChange_shouldReturnForbiddenForNonOwner() throws Exception {
        Long BOOKING_ID = 1L;
        // Given
        when(bookingService.statusChange(anyLong(), anyBoolean(), anyLong()))
                .thenThrow(new IncorrectOwnerException("Only owner can change status"));

        // When & Then
        mvc.perform(patch("/bookings/{booking-id}", BOOKING_ID)
                        .header(USER_IDENTIFICATOR_HEADER_NAME, 500L)
                        .param("approved", "true"))
                .andExpect(status().is4xxClientError());
    }
}

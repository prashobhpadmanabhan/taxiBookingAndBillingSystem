package com.edstem.taxiBookingAndBillingSystem.service;

import com.edstem.taxiBookingAndBillingSystem.constant.Status;
import com.edstem.taxiBookingAndBillingSystem.contract.request.BookingRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.response.BookingResponse;
import com.edstem.taxiBookingAndBillingSystem.contract.response.TaxiResponse;
import com.edstem.taxiBookingAndBillingSystem.model.Booking;
import com.edstem.taxiBookingAndBillingSystem.model.Taxi;
import com.edstem.taxiBookingAndBillingSystem.model.User;
import com.edstem.taxiBookingAndBillingSystem.repository.BookingRepository;
import com.edstem.taxiBookingAndBillingSystem.repository.TaxiRepository;
import com.edstem.taxiBookingAndBillingSystem.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class BookingServiceTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock private ModelMapper modelMapper;
    @InjectMocks
    private BookingService bookingService;
    @Mock private TaxiRepository taxiRepository;
    @Mock private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testBookTaxi() {
        User user = new User(1L, "a", "a@gmail.com", "12345", 1000.0);
        Taxi taxi =
                Taxi.builder()
                        .driverName("b")
                        .licenceNumber("4004")
                        .currentLocation("z")
                        .build();

        BookingRequest request = new BookingRequest("z", "u");
        Long userId=1L;
        Long taxiId = 1L;
        Long distance = 8L;
        Double expense = distance * 10D;
        Booking expectedBooking =
                Booking.builder()
                        .user(user)
                        .taxi(taxi)
                        .pickupLocation(request.getPickupLocation())
                        .dropoffLocation(request.getDropoffLocation())
                        .bookingTime(LocalDateTime.parse(LocalDateTime.now().toString()))
                        .fare(expense)
                        .status(Status.CONFIRMED)
                        .build();
        User updatedUser =
                User.builder()
                        .id(userId)
                        .id(taxiId)
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .accountBalance(user.getAccountBalance() - expectedBooking.getFare())
                        .build();

        BookingResponse expectedResponse = modelMapper.map(expectedBooking, BookingResponse.class);
        when(taxiRepository.findById(taxiId)).thenReturn(Optional.of(taxi));
        when(bookingRepository.save(any())).thenReturn(expectedBooking);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(updatedUser);

        BookingResponse actualResponse = bookingService.bookTaxi(request, userId,taxiId,distance);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testBookTaxi_InsufficientFund() {
        User user = new User(1L, "a", "a@gmail.com", "12345", 1000.0);
        Taxi taxi =
                Taxi.builder()
                        .driverName("a")
                        .licenceNumber("4004")
                        .currentLocation("z")
                        .build();

        BookingRequest request = new BookingRequest("r", "t");
        Long taxiId = 1L;
        Long userId = 2L;
        Long distance = 80L;
        when(taxiRepository.findById(taxiId)).thenReturn(Optional.of(taxi));

        assertThrows(
                EntityNotFoundException.class,
                () -> {
                    bookingService.bookTaxi(request, userId, taxiId, distance);
                });
    }

    @Test
    void testGetBookingDetails() {
        Long bookingId = 1L;
        Taxi taxi = new Taxi(1L, "a", "4001", "c");
        User user = new User(1L, "b", "b@email.com", "password", 100.0);
        Booking booking =
                new Booking(
                        1L,
                        user,
                        taxi,
                        "z",
                        "u",
                        120.0,
                        LocalDateTime.now(),
                        Status.CONFIRMED);
        BookingResponse expectedResponse =
                new BookingResponse(
                        1L,
                        2L,
                        3L,
                        "z",
                        "u",
                        2d,
                        "2024-03-05 10:18:28.012172",
                        Status.CONFIRMED);

        when(bookingRepository.findById(Mockito.eq(bookingId))).thenReturn(Optional.of(booking));
        when(modelMapper.map(booking, BookingResponse.class)).thenReturn(expectedResponse);

        BookingResponse actualResponse = bookingService.viewBookingDetails(bookingId);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testCancelBooking() {
        Long bookingId = 1L;
        Long userId = 1L;
        Long taxiId = 1L;
        User user = new User(userId, "a", "a@gmail.com", "password", 100.0);
        Taxi taxi = new Taxi(taxiId, "b", "4004", "q");
        Booking booking =
                Booking.builder()
                        .user(user)
                        .taxi(taxi)
                        .pickupLocation("location1")
                        .dropoffLocation("location2")
                        .fare(15.00)
                        .bookingTime(LocalDateTime.now())
                        .status(Status.CONFIRMED)
                        .build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        String actualResponse = bookingService.cancelBooking(bookingId);

        assertEquals("booking cancelled successfully" + bookingId, actualResponse);
    }

    @Test
    void testSearchNearestTaxi() {
        Taxi taxiOne = new Taxi(1L, "a", "4080", "u");
        Taxi taxiTwo = new Taxi(1L, "b", "8588", "u");

        List<Taxi> availableTaxies = Arrays.asList(taxiOne, taxiTwo);
        when(taxiRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(
                EntityNotFoundException.class, () -> bookingService.searchNearestTaxi("u"));
        when(taxiRepository.findAll()).thenReturn(availableTaxies);

        List<TaxiResponse> expectedResponse =
                availableTaxies.stream()
                        .map(taxi -> modelMapper.map(taxi, TaxiResponse.class))
                        .collect(Collectors.toList());

        List<TaxiResponse> actualResponse = bookingService.searchNearestTaxi("u");
        assertEquals(expectedResponse, actualResponse);
    }
}

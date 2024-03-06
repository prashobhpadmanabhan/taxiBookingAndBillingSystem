package com.edstem.taxiBookingAndBillingSystem.controller;

import com.edstem.taxiBookingAndBillingSystem.constant.Status;
import com.edstem.taxiBookingAndBillingSystem.contract.request.BookingRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.response.BookingResponse;
import com.edstem.taxiBookingAndBillingSystem.contract.response.TaxiResponse;
import com.edstem.taxiBookingAndBillingSystem.service.BookingService;
import com.edstem.taxiBookingAndBillingSystem.service.TaxiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaxiService taxiService;
    @MockBean private BookingService bookingService;
    @Autowired private BookingController bookingController;

    @Test
    void testBookTaxi() throws Exception {
        BookingResponse buildResult =
                BookingResponse.builder()
                        .bookingTime("2024-03-05 15:25:58.032132")
                        .dropoffLocation("a")
                        .fare(15.0d)
                        .id(1L)
                        .pickupLocation("b")
                        .status(Status.CANCELLED)
                        .taxiId(1L)
                        .userId(1L)
                        .build();
        when(bookingService.bookTaxi(
                Mockito.<BookingRequest>any(),
                Mockito.<Long>any(),
                Mockito.<Long>any(),
                Mockito.<Long>any()))
                .thenReturn(buildResult);

        MockHttpServletRequestBuilder postResult =
                MockMvcRequestBuilders.post("/booking/{userId}/{taxiId}", 1L, 1L);
        MockHttpServletRequestBuilder contentTypeResult =
                postResult
                        .param("distance", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder =
                contentTypeResult.content(objectMapper.writeValueAsString(new BookingRequest()));

        MockMvcBuilders.standaloneSetup(bookingController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(
                        MockMvcResultMatchers.content()
                                .string(
                                        "{\"id\":1,\"userId\":1,\"taxiId\":1,\"pickupLocation\":\"a\",\"dropoffLocation\":\"b\",\"fare\":100.0,\"bookingTime\":\"2024-03-05"
                                                + " 15:25:58.032132\",\"status\":\"CANCELLED\"}"));
    }

    @Test
    void testViewBookingDetails() throws Exception {
        Long id = 1L;
        BookingResponse bookingResponse =
                new BookingResponse(
                        1L,
                        2L,
                        3L,
                        "a",
                        "b",
                        100.0,
                        "2024-03-06 10:18:28.012172",
                        Status.CONFIRMED);

        when(bookingService.viewBookingDetails(id)).thenReturn(bookingResponse);

        mockMvc.perform(get("/booking/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(bookingResponse)));
    }

    @Test
    void testCancelBooking() throws Exception {
        Long id = 1L;
        String expectedResponse = "Booking with ID: " + id + " has been cancelled successfully.";

        when(bookingService.cancelBooking(id)).thenReturn(expectedResponse);

        mockMvc.perform(put("/booking/cancel/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void testSearchNearestTaxi() throws Exception {
        String pickupLocation = "a";
        List<TaxiResponse> expectedResponse =
                Collections.singletonList(new TaxiResponse(1L, "z", "4001", "u"));

        when(bookingService.searchNearestTaxi(pickupLocation)).thenReturn(expectedResponse);

        mockMvc.perform(
                        get("/booking/searchTaxi")
                                .param("pickupLocation", String.valueOf(pickupLocation))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
}

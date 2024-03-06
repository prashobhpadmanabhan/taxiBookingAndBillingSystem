package com.edstem.taxiBookingAndBillingSystem.controller;

import com.edstem.taxiBookingAndBillingSystem.contract.request.TaxiRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.response.TaxiResponse;
import com.edstem.taxiBookingAndBillingSystem.service.TaxiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TaxiControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaxiService taxiService;

    @Test
    void testAddingTaxi() throws Exception {
        TaxiRequest request = new TaxiRequest("z", "40004", "u");
        TaxiResponse expectedResponse = new TaxiResponse(1L, "a", "4004", "w");

        when(taxiService.addTaxi(any(TaxiRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        post("/taxi")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }
}

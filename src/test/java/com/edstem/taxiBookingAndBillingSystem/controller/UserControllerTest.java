package com.edstem.taxiBookingAndBillingSystem.controller;

import com.edstem.taxiBookingAndBillingSystem.contract.request.LoginRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.request.RegisterRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.request.UpdateAccountRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.response.LoginResponse;
import com.edstem.taxiBookingAndBillingSystem.contract.response.SignupResponse;
import com.edstem.taxiBookingAndBillingSystem.contract.response.UpdateAccountResponse;
import com.edstem.taxiBookingAndBillingSystem.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void testSignUp() throws Exception {
        RegisterRequest signupRequest = new RegisterRequest("a", "a@gmail.com", "password");
        SignupResponse expectedResponse = new SignupResponse(1L, "a", "a@gmail.com");
        when(userService.signUpUser(any(RegisterRequest.class))).thenReturn(expectedResponse);
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(
                        org.springframework.test.web.servlet.result.MockMvcResultMatchers.content()
                                .json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest("a@gmail.com", "password");
        LoginResponse expectedResponse =
                new LoginResponse(
                        "name",
                        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcnVuQGdtYWlsLmNvbSIsImlhdCI6MTcwOTA5MDUxNCwiZXhwIjoxNzA5MDk0MTE0fQ.bXQILov9A_5HYr87FJqW3ciKG7mBSA2Q2ORnQCAx2Pc");
        when(userService.loginUser(any(LoginRequest.class))).thenReturn(expectedResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/user/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

    @Test
    void testUpdateBalance() throws Exception {
        Long id = 1L;
        UpdateAccountRequest request = new UpdateAccountRequest(100.0);
        UpdateAccountResponse expectedResponse = new UpdateAccountResponse(666);

        when(userService.updateAccountBalance(id, request)).thenReturn(expectedResponse);

        mockMvc.perform(
                        put("/user/update/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(expectedResponse)));
    }

}

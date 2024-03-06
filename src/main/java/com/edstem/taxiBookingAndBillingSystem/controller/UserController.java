package com.edstem.taxiBookingAndBillingSystem.controller;

import com.edstem.taxiBookingAndBillingSystem.contract.request.LoginRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.request.RegisterRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.request.UpdateAccountRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.response.LoginResponse;
import com.edstem.taxiBookingAndBillingSystem.contract.response.SignupResponse;
import com.edstem.taxiBookingAndBillingSystem.contract.response.UpdateAccountResponse;
import com.edstem.taxiBookingAndBillingSystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public SignupResponse SignUp(@Valid @RequestBody RegisterRequest request) {
        return userService.signUpUser(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) throws Exception {
        return userService.loginUser(request);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UpdateAccountResponse> updateBalance(
            @PathVariable Long id, @RequestBody UpdateAccountRequest request) {
        return ResponseEntity.ok(userService.updateAccountBalance(id, request));
    }
}

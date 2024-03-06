package com.edstem.taxiBookingAndBillingSystem.controller;

import com.edstem.taxiBookingAndBillingSystem.contract.request.TaxiRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.response.TaxiResponse;
import com.edstem.taxiBookingAndBillingSystem.service.TaxiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/taxi")
@RequiredArgsConstructor
public class TaxiController {
    private final TaxiService taxiService;

    @PostMapping
    public ResponseEntity<TaxiResponse> addTaxi(@RequestBody TaxiRequest request) {
        return ResponseEntity.ok(taxiService.addTaxi(request));
    }
}

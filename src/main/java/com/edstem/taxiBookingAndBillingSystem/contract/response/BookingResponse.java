package com.edstem.taxiBookingAndBillingSystem.contract.response;

import com.edstem.taxiBookingAndBillingSystem.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {
    private Long id;
    private Long userId;
    private Long taxiId;
    private String pickupLocation;
    private String dropoffLocation;
    private double fare;
    private String bookingTime;
    private Status status;
}

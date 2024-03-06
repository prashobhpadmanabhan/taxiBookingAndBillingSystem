package com.edstem.taxiBookingAndBillingSystem.model;

import com.edstem.taxiBookingAndBillingSystem.constant.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne private Taxi taxi;
    private String pickupLocation;
    private String dropoffLocation;
    private double fare;
    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    private Status status;
}

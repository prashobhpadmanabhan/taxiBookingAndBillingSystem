package com.edstem.taxiBookingAndBillingSystem.repository;

import com.edstem.taxiBookingAndBillingSystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}

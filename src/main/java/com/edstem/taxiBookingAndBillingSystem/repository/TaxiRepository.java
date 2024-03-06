package com.edstem.taxiBookingAndBillingSystem.repository;

import com.edstem.taxiBookingAndBillingSystem.model.Taxi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxiRepository extends JpaRepository<Taxi, Long> {
    boolean existsByLicenceNumber(String licenceNumber);
}

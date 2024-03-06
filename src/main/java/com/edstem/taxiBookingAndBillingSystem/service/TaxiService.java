package com.edstem.taxiBookingAndBillingSystem.service;

import com.edstem.taxiBookingAndBillingSystem.contract.request.TaxiRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.response.TaxiResponse;
import com.edstem.taxiBookingAndBillingSystem.exception.EntityAlreadyExistsException;
import com.edstem.taxiBookingAndBillingSystem.model.Taxi;
import com.edstem.taxiBookingAndBillingSystem.repository.TaxiRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxiService {
    private final TaxiRepository taxiRepository;
    private final ModelMapper modelMapper;

    public TaxiResponse addTaxi(TaxiRequest request) {
        if (taxiRepository.existsByLicenceNumber(request.getLicenceNumber())) {
            throw new EntityAlreadyExistsException("already exist");
        }
        Taxi taxi = modelMapper.map(request, Taxi.class);
        taxi = taxiRepository.save(taxi);
        return modelMapper.map(taxi, TaxiResponse.class);
    }
}

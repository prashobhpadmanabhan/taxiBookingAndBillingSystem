package com.edstem.taxiBookingAndBillingSystem.service;

import com.edstem.taxiBookingAndBillingSystem.contract.request.TaxiRequest;
import com.edstem.taxiBookingAndBillingSystem.contract.response.TaxiResponse;
import com.edstem.taxiBookingAndBillingSystem.exception.EntityAlreadyExistsException;
import com.edstem.taxiBookingAndBillingSystem.model.Taxi;
import com.edstem.taxiBookingAndBillingSystem.repository.TaxiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TaxiServiceTest {
    private TaxiRepository taxiRepository;
    private ModelMapper modelMapper;
    private TaxiService taxiService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        taxiRepository = Mockito.mock(TaxiRepository.class);
        modelMapper = new ModelMapper();
        taxiService = new TaxiService(taxiRepository, modelMapper);
    }

    @Test
    void testAddTaxi1() {
        TaxiRequest request = new TaxiRequest("a", "b", "c");
        Taxi taxi = modelMapper.map(request, Taxi.class);
        TaxiResponse expectedResponse = modelMapper.map(taxi, TaxiResponse.class);

        when(taxiRepository.existsByLicenceNumber(request.getLicenceNumber())).thenReturn(true);
        assertThrows(EntityAlreadyExistsException.class, () -> taxiService.addTaxi(request));

        when(taxiRepository.existsByLicenceNumber(request.getLicenceNumber())).thenReturn(false);
        when(taxiRepository.save(any())).thenReturn(taxi);

        TaxiResponse actualResponse = taxiService.addTaxi(request);

        assertEquals(expectedResponse.getDriverName(), actualResponse.getDriverName());
        assertEquals(expectedResponse.getLicenceNumber(), actualResponse.getLicenceNumber());
        assertEquals(expectedResponse.getCurrentLocation(), actualResponse.getCurrentLocation());
    }
}

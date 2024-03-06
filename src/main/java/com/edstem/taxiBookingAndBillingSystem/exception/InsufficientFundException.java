package com.edstem.taxiBookingAndBillingSystem.exception;

public class InsufficientFundException extends RuntimeException {
    public InsufficientFundException() {
        super("Insufficient balance");
    }
}

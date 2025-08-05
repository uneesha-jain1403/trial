package com.eazybank.accounts.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class CustomerAlreadyExistsException extends RuntimeException {
    public CustomerAlreadyExistsException(String mobileNumber) {
        super(String.format("Customer already exists for mobile number - %s", mobileNumber));
    }
}
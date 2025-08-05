package com.eazybank.accounts.service;

import com.eazybank.accounts.dto.CustomerDto;

public interface AccountsService {
    Long create(CustomerDto customerDto);
    CustomerDto fetch(String mobileNumber);
    Boolean delete(String mobileNumber);
    Boolean update(CustomerDto customerDto);
}
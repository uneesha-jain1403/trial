package com.eazybank.accounts.service.impl;

import com.eazybank.accounts.dto.AccountsDto;
import com.eazybank.accounts.dto.CustomerDto;
import com.eazybank.accounts.entity.Accounts;
import com.eazybank.accounts.entity.Customer;
import com.eazybank.accounts.exception.CustomerAlreadyExistsException;

import com.eazybank.accounts.mapper.AccountsMapper;
import com.eazybank.accounts.mapper.CustomerMapper;
import com.eazybank.accounts.repository.AccountsRepository;
import com.eazybank.accounts.repository.CustomerRepository;
import com.eazybank.accounts.service.AccountsService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

//@AllArgsConstructor
@NoArgsConstructor
@Service
public class AccountsServiceImpl implements AccountsService {

    private CustomerRepository customerRepository;
    private AccountsRepository accountsRepository;

    @Autowired
    public AccountsServiceImpl(CustomerRepository customerRepository, AccountsRepository accountsRepository) {
        this.customerRepository = customerRepository;
        this.accountsRepository = accountsRepository;
    }

    @Override
    public Long create(CustomerDto customerDto) {
        String mobileNumber = customerDto.getMobileNumber();
        Optional<Customer> foundCustomer = customerRepository.findByMobileNumber(mobileNumber);
        if (foundCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException(mobileNumber);
        }
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Customer savedCustomer = customerRepository.save(customer);
        AccountsDto accountsDto = openAccount(savedCustomer.getCustomerId());
        Accounts accounts = AccountsMapper.mapToAccounts(accountsDto, new Accounts());
        accounts.setCustomerId(savedCustomer.getCustomerId());
        Accounts savedAccount = accountsRepository.save(accounts);
        return savedAccount.getAccountId();
    }

    public AccountsDto openAccount(long customerId) {
        AccountsDto accountsDto = new AccountsDto();
        accountsDto.setAccountType("SAVINGS");
        accountsDto.setBranchAddress("201, Main Road, Wakad, Pune");
        long randomNumber = new Random().nextInt(900000000) + 10000000;
        accountsDto.setAccountNumber(randomNumber);
        return accountsDto;
    }

    @Override
    public CustomerDto fetch(String mobileNumber) {
        Customer foundCustomer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new RuntimeException("Customer not found for given mobile number - " + mobileNumber)
        );
        Accounts foundAccount = accountsRepository.findByCustomerId(foundCustomer.getCustomerId()).orElseThrow(
                () -> new RuntimeException("Account details not found for customer id - " + foundCustomer.getCustomerId())
        );
        AccountsDto accountsDto = AccountsMapper.mapToAccountsDto(foundAccount, new AccountsDto());
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(foundCustomer, new CustomerDto());
        customerDto.setAccountsDto(accountsDto);
        return customerDto;
    }

    @Override
    public Boolean delete(String mobileNumber) {
        boolean isDeleted = false;
        Optional<Customer> foundCustomer = customerRepository.findByMobileNumber(mobileNumber);
        if (foundCustomer.isPresent()) {
            Accounts foundAccount = accountsRepository.findByCustomerId(foundCustomer.get().getCustomerId()).orElseThrow(
                    () -> new RuntimeException("Accounts not found for customer id - " + foundCustomer.get().getCustomerId())
            );
            accountsRepository.delete(foundAccount);
            customerRepository.delete(foundCustomer.get());
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public Boolean update(CustomerDto customerDto) {
        boolean isUpdated = false;
        Optional<Customer> foundCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (foundCustomer.isPresent()) {
            if(customerDto.getName() != null) {
                foundCustomer.get().setName(customerDto.getName());
            }
            if(customerDto.getEmail() != null) {
                foundCustomer.get().setEmail(customerDto.getEmail());
            }
            customerRepository.save(foundCustomer.get());
            isUpdated = true;
        }
        return isUpdated;
    }

}
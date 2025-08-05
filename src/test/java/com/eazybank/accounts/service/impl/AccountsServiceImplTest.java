package com.eazybank.accounts.service.impl;

import com.eazybank.accounts.dto.CustomerDto;
import com.eazybank.accounts.entity.Accounts;
import com.eazybank.accounts.entity.Customer;
import com.eazybank.accounts.mapper.CustomerMapper;
import com.eazybank.accounts.repository.AccountsRepository;
import com.eazybank.accounts.repository.CustomerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AccountsServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    AccountsRepository accountsRepository;

    @InjectMocks
    AccountsServiceImpl accountsService = new AccountsServiceImpl();

    Customer customer;
    Accounts accounts;

    @BeforeEach
    public void beforeEach(){
        customer = new Customer();
        customer.setName("Monica");
        customer.setEmail("monica@test");
        customer.setMobileNumber("9876543210");

        accounts = new Accounts();
        accounts.setAccountType("SAVINGS");
        accounts.setAccountNumber(9876543L);
        accounts.setBranchAddress("201 Main Road, Pune");
    }

    @AfterEach
    public void cleanUp(){
        customer = null;
        accounts = null;
    }

    @Test
    void jpaRepoTest() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        Customer savedCustomer = customerRepository.save(customer);
        assertThat(savedCustomer).isNotNull();
    }


    @Test
    @DisplayName("CREATE - Should create customer record and open the account")
    void create() {

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(accountsRepository.save(any(Accounts.class))).thenReturn(accounts);

        CustomerDto customerDto = new CustomerDto();
        CustomerMapper.mapToCustomerDto(customer, customerDto);
        Long accountId = accountsService.create(customerDto);

        assertThat(accountId).isNotNull();
    }

    @Test
    @DisplayName("READ - should fetch customer by mobile number")
    void fetch(){
        when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(customer));
        when(accountsRepository.findByCustomerId(anyLong())).thenReturn(Optional.of(accounts));

        CustomerDto customerDto = accountsService.fetch(customer.getMobileNumber());

        assertThat(customerDto).isNotNull();
        assertThat(customerDto.getName()).isEqualTo("Monica");
        assertThat(customerDto.getEmail()).isEqualTo("monica@test");
    }

    @Test
    @DisplayName("DELETE - should delete customer and account based on mobile number")
    void delete(){
        when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(customer));
        when(accountsRepository.findByCustomerId(anyLong())).thenReturn(Optional.of(accounts));

        Boolean isDeleted = accountsService.delete(customer.getMobileNumber());
        assertThat(isDeleted).isTrue();
    }

    @Test
    @DisplayName("UPDATE - should update the customer details")
    void update(){
        when(customerRepository.findByMobileNumber(anyString())).thenReturn(Optional.of(customer));

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setEmail("ross@test");
        customerDto.setName("ross");
        Boolean isUpdated = accountsService.update(customerDto);

        Customer customer1 = customerRepository.findByMobileNumber(customer.getMobileNumber()).get();

        assertThat(isUpdated).isTrue();
        assertThat(customer1.getEmail()).isEqualTo(customerDto.getEmail());
        assertThat(customer1.getName()).isEqualTo(customerDto.getName());
    }
}
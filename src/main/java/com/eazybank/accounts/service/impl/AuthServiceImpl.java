package com.eazybank.accounts.service.impl;

import com.eazybank.accounts.controller.AccountsController;
import com.eazybank.accounts.entity.Users;
import com.eazybank.accounts.repository.UsersRepository;
import com.eazybank.accounts.service.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsersRepository usersRepository;

    AuthServiceImpl(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }

    @Override
    public boolean userLogin(AccountsController.LoginRequest loginRequest) {
        Users foundUser = this.usersRepository.findByUsername(loginRequest.username()).orElseThrow(
                () -> new RuntimeException("User not found - " + loginRequest.username())
        );
        System.out.println(foundUser);
        return true;
    }
}
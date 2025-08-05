package com.eazybank.accounts.service;


import com.eazybank.accounts.controller.AccountsController;

public interface AuthService {

    boolean userLogin(AccountsController.LoginRequest loginRequest);
}
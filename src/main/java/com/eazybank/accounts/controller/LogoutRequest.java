package com.eazybank.accounts.controller;

import org.springframework.http.HttpStatus;

public record LogoutRequest(String message, HttpStatus status) {
}
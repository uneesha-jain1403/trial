package com.eazybank.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponseDto {
    private String errorMessage;
    private String apiPath;
    private HttpStatus status;
    private LocalTime timestamp;
}
package com.eazybank.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountsDto {
    private long accountNumber;
    private String accountType;
    private String branchAddress;
}

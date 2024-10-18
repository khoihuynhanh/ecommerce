package com.ecommerce.mappers;

import com.ecommerce.dtos.AccountDTO;
import com.ecommerce.entities.Account;
import com.ecommerce.models.TokenPayload;
import com.ecommerce.responses.AccountResponse;

public class AccountMapper {
    public static Account toAccount(AccountDTO accountDTO) {
        return Account.builder()
                .email(accountDTO.getEmail())
                .username(accountDTO.getUsername())
                .password(accountDTO.getPassword())
                .build();
    }

    public static AccountResponse toAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .email(account.getEmail())
                .username(account.getUsername())
                .role(RoleMapper.toRoleResponse(account.getRole()))
                .build();
    }

    public static TokenPayload toTokenPayload(Account existingAccount) {
        return TokenPayload.builder()
                .accountId(existingAccount.getId())
                .email(existingAccount.getEmail())
                .build();
    }
}

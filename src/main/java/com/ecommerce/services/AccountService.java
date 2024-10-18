package com.ecommerce.services;

import com.ecommerce.dtos.AccountDTO;
import com.ecommerce.dtos.LoginDTO;
import com.ecommerce.responses.AccountResponse;
import com.ecommerce.responses.LoginResponse;

import java.util.List;

public interface AccountService {
    AccountResponse createAccount(AccountDTO accountDTO);

    List<AccountResponse> getAllAccounts();

    AccountResponse getAccountById(int id);

    LoginResponse login(LoginDTO loginDTO);

    AccountResponse updateAccount(AccountDTO accountDTO, int id);

    AccountResponse deleteAccount(int id);
}

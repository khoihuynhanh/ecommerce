package com.ecommerce.controllers;

import com.ecommerce.dtos.AccountDTO;
import com.ecommerce.dtos.LoginDTO;
import com.ecommerce.responses.AccountResponse;
import com.ecommerce.responses.LoginResponse;
import com.ecommerce.services.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody AccountDTO accountDTO
    ) {
        AccountResponse accountResponse = accountService.createAccount(accountDTO);
        return ResponseEntity.ok(accountResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginDTO loginDTO
    ) {
        LoginResponse loginResponse = accountService.login(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }
}

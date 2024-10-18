package com.ecommerce.controllers;

import com.ecommerce.dtos.AccountDTO;
import com.ecommerce.responses.AccountResponse;
import com.ecommerce.services.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/accounts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {
    AccountService accountService;

    @GetMapping("")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accountResponseList = accountService.getAllAccounts();
        return ResponseEntity.ok(accountResponseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable("id") int id
    ) {
        AccountResponse accountResponse = accountService.getAccountById(id);
        return ResponseEntity.ok(accountResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @RequestBody AccountDTO accountDTO,
            @PathVariable("id") int id
    ) {
        AccountResponse update = accountService.updateAccount(accountDTO, id);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AccountResponse> deleteAccount(
            @PathVariable("id") int id
    ) {
        AccountResponse delete = accountService.deleteAccount(id);
        return ResponseEntity.ok(delete);
    }
}

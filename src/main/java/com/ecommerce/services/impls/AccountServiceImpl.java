package com.ecommerce.services.impls;

import com.ecommerce.dtos.AccountDTO;
import com.ecommerce.dtos.LoginDTO;
import com.ecommerce.entities.Account;
import com.ecommerce.entities.Role;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.mappers.AccountMapper;
import com.ecommerce.repositories.AccountRepository;
import com.ecommerce.repositories.RoleRepository;
import com.ecommerce.responses.AccountResponse;
import com.ecommerce.responses.LoginResponse;
import com.ecommerce.services.AccountService;
import com.ecommerce.utils.JwtTokenUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    JwtTokenUtil jwtTokenUtil;

    @Override
    public AccountResponse createAccount(AccountDTO accountDTO) {
        validateAccountDTO(accountDTO);

        Role defaultRole = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("not found role id"));
        Account account = AccountMapper.toAccount(accountDTO);
        account.setRole(defaultRole);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account = accountRepository.save(account);
        return AccountMapper.toAccountResponse(account);
    }

    private void validateAccountDTO(AccountDTO accountDTO) {
        if (accountDTO.getEmail() == null) {
            throw EcommerceException.badRequestException("Email must be not null");
        }
        if (accountDTO.getUsername() == null) {
            throw EcommerceException.badRequestException(("Username must be not null"));
        }
        if (accountDTO.getPassword() == null) {
            throw EcommerceException.badRequestException("Password must be not null");
        }
        if (accountRepository.existsByEmail(accountDTO.getEmail())) {
            throw EcommerceException.badRequestException("Email already exists");
        }
        if (accountRepository.existsByUsername(accountDTO.getUsername())) {
            throw EcommerceException.badRequestException("Username already exists");
        }
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        List<Account> accountResponseList = accountRepository.findAll();
        return accountResponseList.stream().map(AccountMapper::toAccountResponse).toList();
    }

    @Override
    public AccountResponse getAccountById(int id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("Account not found"));
        return AccountMapper.toAccountResponse(account);
    }

    @Override
    public LoginResponse login(LoginDTO loginDTO) {
//        Account account = accountRepository.findByEmail(loginDTO.getEmail());
//        boolean isAuthentication = passwordEncoder
//                .matches(loginDTO.getPassword(), account.getPassword());
//        if (!isAuthentication) {
//            throw EcommerceException.badRequestException("password does not match");
//        }
        Account existingAccount = validateAndGetAccount(loginDTO);
        final long ONE_DAY = 24 * 60 * 60;
        String accessToken = jwtTokenUtil.generateToken(AccountMapper.toTokenPayload(existingAccount), ONE_DAY);
        return LoginResponse.builder()
                .accessToken(accessToken)
                .accountResponse(AccountMapper.toAccountResponse(existingAccount))
                .build();
    }

    private Account validateAndGetAccount(LoginDTO loginDTO) {
        if (!accountRepository.existsByEmail(loginDTO.getEmail())) {
            throw EcommerceException.badRequestException("Invalid email");
        }

        Account existingAccount = accountRepository.findByEmail(loginDTO.getEmail());
        boolean isAuthentication = passwordEncoder.matches(loginDTO.getPassword(), existingAccount.getPassword());
        if (!isAuthentication) {
            throw EcommerceException.badRequestException("Invalid password");
        }

        return existingAccount;
    }


    @Override
    public AccountResponse updateAccount(AccountDTO accountDTO, int id) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("Not found account"));
        Account account = AccountMapper.toAccount(accountDTO);
        account.setId(existingAccount.getId());
        account.setRole(existingAccount.getRole());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
        return AccountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse deleteAccount(int id) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> EcommerceException.notFoundException("Not found id"));
        accountRepository.delete(existingAccount);
        return AccountMapper.toAccountResponse(existingAccount);
    }
}

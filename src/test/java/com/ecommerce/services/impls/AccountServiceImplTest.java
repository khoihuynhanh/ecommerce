package com.ecommerce.services.impls;

import com.ecommerce.dtos.AccountDTO;
import com.ecommerce.dtos.LoginDTO;
import com.ecommerce.entities.Account;
import com.ecommerce.entities.Role;
import com.ecommerce.exceptions.EcommerceException;
import com.ecommerce.repositories.AccountRepository;
import com.ecommerce.repositories.RoleRepository;
import com.ecommerce.responses.AccountResponse;
import com.ecommerce.utils.JwtTokenUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith({MockitoExtension.class})
class AccountServiceImplTest {
    @InjectMocks
    AccountServiceImpl accountService;

    @Mock
    AccountRepository accountRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtTokenUtil jwtTokenUtil;

    @Test
    void createAccount_with_email_is_null() {
        AccountDTO accountDTO = AccountDTO.builder()
                .email(null)
                .username("username")
                .password("password")
                .build();

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.createAccount(accountDTO)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Email must be not null", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.never()).save(any(Account.class));
    }

    @Test
    void createAccount_with_username_is_null() {
        AccountDTO accountDTO = AccountDTO.builder()
                .email("email")
                .username(null)
                .password("password")
                .build();

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.createAccount(accountDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Username must be not null", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.never()).save(any(Account.class));
    }

    @Test
    void createAccount_with_password_is_null() {
        AccountDTO accountDTO = AccountDTO.builder()
                .email("email")
                .username("username")
                .password(null)
                .build();

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.createAccount(accountDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Password must be not null", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.never()).save(any(Account.class));
    }

    @Test
    void createAccount_with_existing_email() {
        AccountDTO accountDTO = AccountDTO.builder()
                .email("existingEmail")
                .username("username")
                .password("password")
                .build();

        Mockito.when(accountRepository.existsByEmail("existingEmail"))
                .thenReturn(true);

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.createAccount(accountDTO)
        );
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Email already exists", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.never()).save(any(Account.class));
    }

    @Test
    void createAccount_with_existing_username() {
        AccountDTO accountDTO = AccountDTO.builder()
                .email("email")
                .username("existingUsername")
                .password("password")
                .build();

        Mockito.when(accountRepository.existsByUsername("existingUsername"))
                .thenReturn(true);
        Mockito.when(accountRepository.existsByEmail("email"))
                .thenReturn(false);

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.createAccount(accountDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Username already exists", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.never()).save(any(Account.class));
    }

    @Test
    void createAccount_success() {
        AccountDTO accountDTO = AccountDTO.builder()
                .email("email")
                .username("username")
                .password("password")
                .build();
        Role defaultRole = Role.builder()
                .id(1)
                .name("user")
                .build();
        Account savedAccount = Account.builder()
                .id(1)
                .email("email")
                .username("username")
                .password("password")
                .role(defaultRole)
                .build();

        Mockito.when(accountRepository.existsByEmail("email"))
                .thenReturn(false);
        Mockito.when(accountRepository.existsByUsername("username"))
                .thenReturn(false);
        Mockito.when(roleRepository.findById(1))
                        .thenReturn(Optional.of(defaultRole));
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);

        AccountResponse accountResponse = accountService.createAccount(accountDTO);
        Mockito.verify(accountRepository, Mockito.times(1))
                .save(any(Account.class));
        Assertions.assertEquals(1, accountResponse.getId());
        Assertions.assertEquals("username", accountResponse.getUsername());
        Assertions.assertEquals("email", accountResponse.getEmail());
        Assertions.assertEquals("user", accountResponse.getRole().getName());
    }

    @Test
    void getAllAccounts_success() {
        Role defaultRole = Role.builder()
                .id(1)
                .name("user")
                .build();
        Account account1 = Account.builder()
                .id(1)
                .email("email1")
                .username("username1")
                .password("password1")
                .role(defaultRole)
                .build();
        Account account2 = Account.builder()
                .id(1)
                .email("email2")
                .username("username2")
                .password("password2")
                .role(defaultRole)
                .build();
        List<Account> accountList = List.of(account1, account2);

        Mockito.when(accountRepository.findAll())
                .thenReturn(accountList);

        List<AccountResponse> accountResponseList = accountService.getAllAccounts();

        Assertions.assertEquals(2, accountResponseList.size());
        Assertions.assertEquals("username1", accountResponseList.get(0).getUsername());
        Assertions.assertEquals("username2", accountResponseList.get(1).getUsername());
        Mockito.verify(accountRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void getAccountById_notFound() {
        int id = 1;

        Mockito.when(accountRepository.findById(id))
                .thenReturn(Optional.empty());

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.getAccountById(id)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Account not found", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void getAccountById_success() {
        int id = 1;

        Role defaultRole = Role.builder()
                .id(1)
                .name("user")
                .build();

        Account existingAccount = Account.builder()
                .id(1)
                .email("email")
                .username("username")
                .password("password")
                .role(defaultRole)
                .build();

        Mockito.when(accountRepository.findById(id))
                .thenReturn(Optional.of(existingAccount));

        AccountResponse actual = accountService.getAccountById(id);

        Assertions.assertEquals(1, actual.getId());
        Assertions.assertEquals("username", actual.getUsername());
        Assertions.assertEquals("email", actual.getEmail());

        Mockito.verify(accountRepository, Mockito.times(1))
                .findById(id);
    }

    @Test
    void login_invalid_email() {
        LoginDTO loginDTO = LoginDTO.builder()
                .email("email")
                .password("password")
                .build();

        Mockito.when(accountRepository.existsByEmail("email"))
                .thenReturn(true);

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.login(loginDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Invalid email", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.times(1))
                .existsByEmail("email");
    }

    @Test
    void login_invalid_password() {
        LoginDTO loginDTO = LoginDTO.builder()
                .email("email")
                .password("wrongPassword")
                .build();

        Role defaultRole = Role.builder()
                .id(1)
                .name("user")
                .build();

        Account existingAccount = Account.builder()
                .id(1)
                .email("email")
                .username("username")
                .password(passwordEncoder.encode("password"))
                .role(defaultRole)
                .build();

        Mockito.when(accountRepository.findByEmail(loginDTO.getEmail()))
                .thenReturn(existingAccount);

        Mockito.when(passwordEncoder.matches(loginDTO.getPassword(), existingAccount.getPassword()))
                .thenReturn(false);

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.login(loginDTO)
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actualThrow.getStatus());
        Assertions.assertEquals("Invalid password", actualThrow.getError().getMessage());

        Mockito.verify(accountRepository, Mockito.times(1))
                .findByEmail(loginDTO.getEmail());
        Mockito.verify(passwordEncoder, Mockito.times(1))
                .matches(loginDTO.getPassword(), existingAccount.getPassword());
        Mockito.verify(jwtTokenUtil, Mockito.never())
                .generateToken(any(), anyLong());
    }


    @Test
    void updateAccount_notFound() {
        int id = 1;
        AccountDTO accountDTO = AccountDTO.builder().build();

        Mockito.when(accountRepository.findById(id))
                .thenReturn(Optional.empty());

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.updateAccount(accountDTO, id)
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Not found account", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.times(1))
                .findById(1);
    }

    @Test
    void updateAccount_success() {
        AccountDTO accountDTO = AccountDTO.builder()
                .email("email")
                .username("username")
                .password("password")
                .build();

        Role defaultRole = Role.builder()
                .id(1)
                .name("user")
                .build();

        Account existingAccount = Account.builder()
                .id(1)
                .email("email")
                .username("username")
                .password(passwordEncoder.encode("password"))
                .role(defaultRole)
                .build();

        Mockito.when(accountRepository.findById(1))
                .thenReturn(Optional.of(existingAccount));
        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(existingAccount);

        AccountResponse update = accountService.updateAccount(accountDTO, 1);
        Assertions.assertEquals(existingAccount.getEmail(), update.getEmail());
        Assertions.assertEquals(existingAccount.getUsername(), update.getUsername());
        Mockito.verify(accountRepository, Mockito.times(1)).findById(1);
        Mockito.verify(accountRepository, Mockito.times(1)).save(any(Account.class));
    }

    @Test
    void deleteAccount_success() {
        Account account = Account.builder()
                .id(1)
                .email("email")
                .username("username")
                .password("password")
                .role(new Role(1, "user"))
                .build();

        Mockito.when(accountRepository.findById(1))
                .thenReturn(Optional.of(account));

        AccountResponse delete = accountService.deleteAccount(1);

        Assertions.assertEquals(account.getId(), delete.getId());
        Mockito.verify(accountRepository, Mockito.times(1))
                .delete(account);
    }

    @Test
    void deleteAccount_notFound() {
        Mockito.when(accountRepository.findById(1))
                .thenReturn(Optional.empty());

        EcommerceException actualThrow = Assertions.assertThrows(
                EcommerceException.class,
                () -> accountService.deleteAccount(1)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, actualThrow.getStatus());
        Assertions.assertEquals("Not found id", actualThrow.getError().getMessage());
        Mockito.verify(accountRepository, Mockito.times(1))
                .findById(1);
    }
}
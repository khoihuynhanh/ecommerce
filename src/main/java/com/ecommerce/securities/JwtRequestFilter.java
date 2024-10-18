package com.ecommerce.securities;

import com.ecommerce.entities.Account;
import com.ecommerce.mappers.AccountMapper;
import com.ecommerce.models.TokenPayload;
import com.ecommerce.repositories.AccountRepository;
import com.ecommerce.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtRequestFilter extends OncePerRequestFilter {
    JwtTokenUtil jwtTokenUtil;
    AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (isByPass(request, response, filterChain)) return;

        String requestHeader = request.getHeader("Authorization");
        String token = null;
        TokenPayload tokenPayload = null;

        // get token from header
        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
            token = requestHeader.split(" ")[1];
            try {
                tokenPayload = jwtTokenUtil.getTokenPayload(token);
            } catch (ExpiredJwtException e) {
                System.out.println("token is expired");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            } catch (Exception e) {
                System.out.println("Invalid token");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } else {
            System.out.println("admin token not found");
        }

        // Authenticate users from tokens
        if (tokenPayload != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Optional<Account> accountOptional = accountRepository.findById(tokenPayload.getAccountId());
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();
                if (jwtTokenUtil.isValid(token, AccountMapper.toTokenPayload(account))) {
                    List<GrantedAuthority> authorities = new ArrayList<>(account.getAuthorities());
                    UserDetails userDetails = new User(
                            account.getEmail(),
                            account.getPassword(),
                            authorities
                    );
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    authorities
                            );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private static boolean isByPass(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain
    ) throws IOException, ServletException {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/api/v1/login", "POST"),
                Pair.of("/api/v1/register", "POST")
        );

        String requestPath = request.getServletPath();
        String requestMethod = request.getMethod();

        for (Pair<String, String> bypassToken : bypassTokens) {
            if (requestPath.equals(bypassToken.getFirst()) && requestMethod.equals(bypassToken.getSecond())) {
                filterChain.doFilter(request, response);
                return true;
            }
        }
        return false;
    }
}

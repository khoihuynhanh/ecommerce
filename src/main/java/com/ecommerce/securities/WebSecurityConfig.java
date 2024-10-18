package com.ecommerce.securities;

import com.ecommerce.entities.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Value("${api.prefix}")
    private String apiPrefix;

    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(requests -> requests
                        // login - register
                        .requestMatchers(HttpMethod.POST, String.format("%s/login", apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.POST, String.format("%s/register", apiPrefix)).permitAll()


                        // account
                        .requestMatchers(HttpMethod.GET, String.format("%s/accounts**", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, String.format("%s/accounts/**", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/accounts/**", apiPrefix)).hasRole(Role.ADMIN)

                        // category
                        .requestMatchers(HttpMethod.POST, String.format("%s/categories", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.GET, String.format("%s/categories**", apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.PUT, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/categories/**", apiPrefix)).hasRole(Role.ADMIN)

                        // product
                        .requestMatchers(HttpMethod.POST, String.format("%s/products", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.POST, String.format("%s/products/generateFakeProducts", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.GET, String.format("%s/products**", apiPrefix)).permitAll()
                        .requestMatchers(HttpMethod.PUT, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/products/**", apiPrefix)).hasRole(Role.ADMIN)

                        // order
                        .requestMatchers(HttpMethod.POST, String.format("%s/orders", apiPrefix)).hasRole(Role.USER)
                        .requestMatchers(HttpMethod.GET, String.format("%s/orders**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, String.format("%s/orders/**", apiPrefix)).hasRole(Role.USER)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)

                        // order detail
                        .requestMatchers(HttpMethod.POST, String.format("%s/order-details", apiPrefix)).hasRole(Role.USER)
                        .requestMatchers(HttpMethod.GET, String.format("%s/order-details**", apiPrefix)).hasAnyRole(Role.USER, Role.ADMIN)
                        .requestMatchers(HttpMethod.PUT, String.format("%s/order-details/**", apiPrefix)).hasRole(Role.USER)
                        .requestMatchers(HttpMethod.DELETE, String.format("%s/order-details", apiPrefix)).hasRole(Role.ADMIN)

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }
}

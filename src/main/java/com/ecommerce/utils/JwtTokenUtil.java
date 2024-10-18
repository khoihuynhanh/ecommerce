package com.ecommerce.utils;

import com.ecommerce.models.TokenPayload;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtTokenUtil {
    @Value("${jwt.secretKey}")
    String secretKey;

    public String generateToken(TokenPayload tokenPayload, long oneDay) {
//        this.generateSecretKey();

        Map<String, Object> claims = new HashMap<>();
        claims.put("payload", tokenPayload);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + oneDay * 1000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String secretKey = Encoders.BASE64.encode(bytes);
        return secretKey;
    }

    public <T> T getClaimsFromToken(String token, Function<Claims, T> claimResolver) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimResolver.apply(claims);
    }

    // check token valid ?
    public boolean isValid(String token, TokenPayload tokenPayloadFromAccount) {
        if (isTokenExpired(token)) {
            return false;
        }
        TokenPayload tokenPayload = getTokenPayload(token);
        return tokenPayload.getAccountId() == tokenPayload.getAccountId()
                && tokenPayload.getEmail().equals(tokenPayloadFromAccount.getEmail());
    }

    public TokenPayload getTokenPayload(String token) {
        return getClaimsFromToken(token, (Claims claims) -> {
            Map<String, Object> result = (Map<String, Object>) claims.get("payload");
            return TokenPayload.builder()
                    .accountId((Integer) result.get("accountId"))
                    .email((String) result.get("email"))
                    .build();
        });
    }

    private boolean isTokenExpired(String token) {
        Date expiredTime = getClaimsFromToken(token, Claims::getExpiration);
        return expiredTime.before(new Date());
    }
}

package com.ecommerce.exceptions;

import com.ecommerce.models.CustomError;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EcommerceException extends RuntimeException {
    HttpStatus status;
    CustomError error;

    public static EcommerceException notFoundException(String message) {
        return EcommerceException.builder().status(HttpStatus.NOT_FOUND)
                .error(CustomError.builder()
                        .code("404")
                        .message(message)
                        .build())
                .build();
    }

    public static EcommerceException badRequestException(String message) {
        return EcommerceException.builder().status(HttpStatus.BAD_REQUEST)
                .error(CustomError.builder()
                        .code("400")
                        .message(message)
                        .build())
                .build();
    }
}

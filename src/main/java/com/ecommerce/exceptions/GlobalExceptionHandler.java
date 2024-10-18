package com.ecommerce.exceptions;

import com.ecommerce.models.CustomError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EcommerceException.class)
    public ResponseEntity<CustomError> handleEcommerceException(EcommerceException ex) {
        return new ResponseEntity<CustomError>(ex.getError(), ex.getStatus());
    }
}

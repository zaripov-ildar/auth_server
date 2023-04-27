package org.zaripov.istore.auth.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.zaripov.istore.auth.dtos.AppException;


/*
    Created by Ildar Zaripov
    at 26.04.2023 12:10 
*/
@ControllerAdvice
@Slf4j
public class GlobalExceptionsHandler {

    @ExceptionHandler
    public ResponseEntity<AppException> handleEmailAlreadyExistsException(EmailAlreadyExists e) {
        return new ResponseEntity<>(new AppException("EMAIL_ALREADY_EXISTS", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppException> handleSignatureException(WrongJwtException e) {
        return new ResponseEntity<>(new AppException("TOTALLY_BOGUS_JWT", e.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}

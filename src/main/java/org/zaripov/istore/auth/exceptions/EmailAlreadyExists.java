package org.zaripov.istore.auth.exceptions;

/* 
    Created by Ildar Zaripov
    at 26.04.2023 12:30 
*/
public class EmailAlreadyExists extends RuntimeException {
    public EmailAlreadyExists(String email) {
        super(email);
    }
}

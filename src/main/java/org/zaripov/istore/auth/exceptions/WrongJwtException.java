package org.zaripov.istore.auth.exceptions;

/*
    Created by Ildar Zaripov
    at 27.04.2023 12:13 
*/
public class WrongJwtException extends RuntimeException{
    public WrongJwtException(String message) {
        super(message);
    }
}

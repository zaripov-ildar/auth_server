package org.zaripov.istore.auth.dtos;

public record AuthenticationResponse(String jwt, String refreshToken) {
}

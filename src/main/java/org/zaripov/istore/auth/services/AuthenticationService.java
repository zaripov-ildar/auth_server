package org.zaripov.istore.auth.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zaripov.istore.auth.dtos.AuthenticationRequest;
import org.zaripov.istore.auth.dtos.AuthenticationResponse;
import org.zaripov.istore.auth.entities.Person;
import org.zaripov.istore.auth.entities.Role;
import org.zaripov.istore.auth.exceptions.EmailAlreadyExists;
import org.zaripov.istore.auth.repositories.RoleRepository;
import org.zaripov.istore.auth.utils.JwtUtils;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtils jwtUtils;
    private final PersonService personService;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    @Value("${security_params.initialRole}")
    private String initialRole;

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException{
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String personEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        personEmail = jwtUtils.extractEmail(refreshToken);
        if (personEmail != null) {
            var person = personService.findPersonByEmail(personEmail);
            if (jwtUtils.isTokenValid(refreshToken, person)) {
                var accessToken = jwtUtils.generateJwt(person);
                var authResponse = new AuthenticationResponse(accessToken, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        var person = personService.findPersonByEmail(request.email());
        return createResponse(person);
    }


    public AuthenticationResponse register(AuthenticationRequest request) {
        Role roleClient = roleRepository.findByTitle(initialRole)
                .orElse(new Role("CLIENT"));
        roleRepository.save(roleClient);

        if (personService.isExistByEmail(request.email())) {
            throw new EmailAlreadyExists(request.email());
        }
        Person person = Person.builder()
                .email(request.email())
                .password(encoder.encode(request.password()))
                .roles(List.of(roleClient))
                .build();
        personService.save(person);
        return createResponse(person);
    }

    private AuthenticationResponse createResponse(Person person) {
        var jwt = jwtUtils.generateJwt(person);
        var refreshToken = jwtUtils.generateRefreshToken(person);
        return new AuthenticationResponse(jwt, refreshToken);
    }
}

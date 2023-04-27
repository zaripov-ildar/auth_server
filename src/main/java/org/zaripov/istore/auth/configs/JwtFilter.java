package org.zaripov.istore.auth.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.zaripov.istore.auth.exceptions.WrongJwtException;
import org.zaripov.istore.auth.services.PersonService;
import org.zaripov.istore.auth.utils.JwtUtils;

import java.io.IOException;

/*
    Created by Ildar Zaripov
    at 22.04.2023 14:02
*/

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final PersonService personService;
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        var authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);
            try {
                final String email = jwtUtils.extractEmail(jwt);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails person = personService.findPersonByEmail(email);
                    if (jwtUtils.isTokenValid(jwt, person)) {
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                                person,
                                null,
                                person.getAuthorities()
                        );
                        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(token);
                    }
                }
            } catch (WrongJwtException e){
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}

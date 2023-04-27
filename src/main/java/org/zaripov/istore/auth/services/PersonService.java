package org.zaripov.istore.auth.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.zaripov.istore.auth.entities.Person;
import org.zaripov.istore.auth.repositories.PersonDetailsRepository;

/*
    Created by Ildar Zaripov
    at 22.04.2023 $ 14:15 
*/
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonDetailsRepository repository;

    public Person findPersonByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Can't find the email: " + email));
    }

    public void save(Person person) {
        repository.save(person);
    }

    public boolean isExistByEmail(String email) {
        return repository.existsByEmail(email);
    }
}

package org.zaripov.istore.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zaripov.istore.auth.entities.Person;

import java.util.Optional;

@Repository
public interface PersonDetailsRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByEmail(String userEmail);

    boolean existsByEmail(String email);
}
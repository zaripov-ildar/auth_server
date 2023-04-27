package org.zaripov.istore.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zaripov.istore.auth.entities.Role;

import java.util.Optional;

/*
    Created by Ildar Zaripov
    at 22.04.2023 $ 13:33 
*/

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByTitle(String title);
}

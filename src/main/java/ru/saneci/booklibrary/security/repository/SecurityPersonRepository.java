package ru.saneci.booklibrary.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.saneci.booklibrary.security.domain.SecurityPerson;

import java.util.Optional;

@Repository
public interface SecurityPersonRepository extends JpaRepository<SecurityPerson, Long> {

    Optional<SecurityPerson> findPersonByUsername(String username);
}

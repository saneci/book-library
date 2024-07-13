package ru.saneci.booklibrary.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saneci.booklibrary.security.domain.SecurityPerson;
import ru.saneci.booklibrary.security.repository.SecurityPersonRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityPersonService {

    private final SecurityPersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<SecurityPerson> findByUserName(String username) {
        return personRepository.findPersonByUsername(username);
    }

    @Transactional
    public void createNewUser(SecurityPerson person) {
        log.debug("Request was received to create a new user by username: {}", person.getUsername());
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
        personRepository.save(person);
        log.debug("New user: {} was created successfully", person.getUsername());
    }
}

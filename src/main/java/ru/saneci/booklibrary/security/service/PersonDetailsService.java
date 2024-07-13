package ru.saneci.booklibrary.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saneci.booklibrary.security.domain.SecurityPerson;
import ru.saneci.booklibrary.security.dto.PersonDetails;
import ru.saneci.booklibrary.security.repository.SecurityPersonRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonDetailsService implements UserDetailsService {

    private final SecurityPersonRepository personRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Processing request to search for a user by username '{}'", username);
        Optional<SecurityPerson> person = personRepository.findPersonByUsername(username);
        if (person.isEmpty()) {
            log.info("User with username '{}' not found", username);
            throw new UsernameNotFoundException("User '" + username + "' not found");
        }

        log.info("User with username '{}' was found", username);
        return new PersonDetails(person.get());
    }
}

package ru.saneci.booklibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saneci.booklibrary.domain.Book;
import ru.saneci.booklibrary.domain.Person;
import ru.saneci.booklibrary.domain.Role;
import ru.saneci.booklibrary.repository.PersonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.application.days_until_book_overdue}")
    private int daysToBookOverdue;

    @Autowired
    public PersonService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
    }

    public Optional<Person> findByUserName(String username) {
        return personRepository.findPersonByUsername(username);
    }

    public Optional<Person> findPersonWithBooksById(Long id) {
        Optional<Person> person = personRepository.findPersonWithBooksById(id);

        if (person.isPresent()) {
            List<Book> bookList = person.get().getBookList();
            LocalDateTime overdueDateTime = LocalDateTime.now().minusDays(daysToBookOverdue);
            bookList.forEach(book -> {
                if (book.getGivenAt() != null && book.getGivenAt().isBefore(overdueDateTime)) {
                    book.setOverdue(true);
                }
            });
        }

        return person;
    }

    @Transactional
    public void save(Person person) {
        person.setRole(Role.ROLE_USER);
        encodePassword(person);
        personRepository.save(person);
    }

    @Transactional
    public void update(Person person) {
        encodePassword(person);
        personRepository.save(person);
    }

    @Transactional
    public void delete(Long id) {
        personRepository.deleteById(id);
    }

    private void encodePassword(Person person) {
        String encodedPassword = passwordEncoder.encode(person.getPassword());
        person.setPassword(encodedPassword);
    }
}

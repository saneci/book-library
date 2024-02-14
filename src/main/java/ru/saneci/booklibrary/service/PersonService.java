package ru.saneci.booklibrary.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saneci.booklibrary.domain.Book;
import ru.saneci.booklibrary.domain.Person;
import ru.saneci.booklibrary.repository.PersonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    @Value("${spring.application.days_until_book_overdue}")
    private int daysToBookOverdue;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Optional<Person> findById(Long id) {
        return personRepository.findById(id);
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
        personRepository.save(person);
    }

    @Transactional
    public void update(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}

package ru.saneci.booklibrary.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saneci.booklibrary.business.domain.Book;
import ru.saneci.booklibrary.business.domain.BusinessPerson;
import ru.saneci.booklibrary.business.repository.BusinessPersonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BusinessPersonService {

    private final BusinessPersonRepository personRepository;

    @Value("${spring.application.days_until_book_overdue}")
    private int daysToBookOverdue;

    public List<BusinessPerson> findAll() {
        return personRepository.findAll().stream()
                .filter(person -> person.getRole().equals("ROLE_READER"))
                .toList();
    }

    public Optional<BusinessPerson> findById(Long id) {
        return personRepository.findById(id);
    }

    public Optional<BusinessPerson> findPersonWithBooksById(Long id) {
        Optional<BusinessPerson> person = personRepository.findPersonWithBooksById(id);

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
    public void save(BusinessPerson person) {
        personRepository.save(person);
    }

    @Transactional
    public void update(BusinessPerson person) {
        personRepository.save(person);
    }

    @Transactional
    public void delete(Long id) {
        personRepository.deleteById(id);
    }
}

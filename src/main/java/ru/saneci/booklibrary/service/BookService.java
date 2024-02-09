package ru.saneci.booklibrary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saneci.booklibrary.domain.Book;
import ru.saneci.booklibrary.domain.Person;
import ru.saneci.booklibrary.repository.BookRepository;
import ru.saneci.booklibrary.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> findAll(String title) {
        return bookRepository.findAllByTitleContainsIgnoreCase(title);
    }

    public Page<Book> findAll(int page, int size, boolean sortByYear) {
        return sortByYear
                ? bookRepository.findAll(PageRequest.of(page, size, Sort.by(Direction.ASC, "publishYear")))
                : bookRepository.findAll(PageRequest.of(page, size, Sort.by(Direction.DESC, "publishYear")));
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findBookWithPersonById(Long id) {
        return bookRepository.findBookWithPersonById(id);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book book) {
        if (book.getPerson() == null) {
            bookRepository.findBookWithPersonById(book.getId()).ifPresent(b -> book.setPerson(b.getPerson()));
        }
        bookRepository.save(book);
    }

    @Transactional
    public void updatePersonId(Long bookId, Long personId) {
        Person person = null;

        if (personId != null) {
            person = personRepository.getReferenceById(personId);
        }

        bookRepository.updatePersonId(person, bookId);
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}

package ru.saneci.booklibrary.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saneci.booklibrary.business.domain.Book;
import ru.saneci.booklibrary.business.domain.BusinessPerson;
import ru.saneci.booklibrary.business.repository.BookRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

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
        bookRepository.findById(book.getId()).ifPresent(b -> {
            b.setTitle(book.getTitle());
            b.setAuthor(book.getAuthor());
            b.setPublishYear(book.getPublishYear());
        });
    }

    @Transactional
    public void updatePersonId(Long bookId, BusinessPerson person) {
        bookRepository.findById(bookId).ifPresent(b -> {
            b.setPerson(person);
            if (person != null)
                b.setGivenAt(LocalDateTime.now());
            else
                b.setGivenAt(null);
        });
    }

    @Transactional
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}

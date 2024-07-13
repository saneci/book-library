package ru.saneci.booklibrary.business.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.saneci.booklibrary.business.domain.Book;
import ru.saneci.booklibrary.business.domain.BusinessPerson;
import ru.saneci.booklibrary.business.service.BookService;
import ru.saneci.booklibrary.business.service.BusinessPersonService;
import ru.saneci.booklibrary.business.util.BindingResultLogger;
import ru.saneci.booklibrary.business.util.TemplateUtil;

import java.util.Optional;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BindingResultLogger brLogger = BindingResultLogger.getLogger(BookController.class);

    private static final String NEW_BOOK_VIEW = "books/new";
    private static final String BOOK_LIST_VIEW = "books/list";
    private static final String BOOK_ITEM_VIEW = "books/book";
    private static final String BOOK_UPDATING_VIEW = "books/update";

    private static final String REDIRECT_TO_BOOKS = "redirect:/books";
    private static final String REDIRECT_TO_BOOK = "redirect:/books/{id}";

    private static final String ERROR_404 = "error/not-found";

    private final BookService bookService;
    private final BusinessPersonService personService;

    @GetMapping("/new")
    public String getNewBookView(@ModelAttribute("book") Book book) {
        log.info("Returning new book registration page");
        return NEW_BOOK_VIEW;
    }

    @PostMapping
    public String addNewBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        log.info("Processing create new book with id {}", book.getId());
        if (bindingResult.hasErrors()) {
            brLogger.warn("addNewBook", bindingResult);
            return NEW_BOOK_VIEW;
        }
        bookService.save(book);
        log.info("Book with id {} created", book.getId());

        return REDIRECT_TO_BOOKS;
    }

    @GetMapping({"", "/search"})
    public String getAllBooks(@RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size,
                              @RequestParam(value = "sort_by_year", defaultValue = "false") boolean sortByYear,
                              @RequestParam(value = "title", defaultValue = "") String title, Model model) {
        log.info("Processing a search request for all books");
        Page<Book> bookPage = bookService.findAll(page, size, sortByYear);

        if (bookPage.getTotalPages() > 1) {
            // add attributes for pagination
            model.addAttribute("previousPage", bookPage.previousOrFirstPageable().getPageNumber());
            model.addAttribute("currentPage", bookPage.getNumber());
            model.addAttribute("nextPage", bookPage.nextOrLastPageable().getPageNumber());
            model.addAttribute("lastPage", bookPage.getTotalPages() - 1);
            model.addAttribute("pageNumbers", TemplateUtil.generatePageNumbers(bookPage));
        }

        if (title.isEmpty()) {
            model.addAttribute("bookList", bookPage.getContent());
        } else {
            model.addAttribute("bookList", bookService.findAll(title));
            model.addAttribute("bookTitle", title);
        }

        model.addAttribute("size", size);
        model.addAttribute("sortByYear", sortByYear);
        log.info("Returning search request result with {} books found", bookPage.getTotalElements());

        return BOOK_LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") Long id, Model model, @ModelAttribute("person") BusinessPerson person) {
        log.info("Processing a search request for a book with id {}", id);
        Optional<Book> optionalBook = bookService.findBookWithPersonById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            model.addAttribute("book", book);

            if (book.getPerson() == null) {
                model.addAttribute("people", personService.findAll());
            }
            log.info("Returning book with id {}", id);

            return BOOK_ITEM_VIEW;
        } else {
            log.info("Returning not found response");

            return ERROR_404;
        }
    }

    @PatchMapping("/{id}/assign")
    public String assignBookToTheReader(@PathVariable("id") Long id, @ModelAttribute("person") BusinessPerson person) {
        log.info("Processing assign the book with id {} to the reader with id {}", id, person.getId());
        bookService.updatePersonId(id, person);
        log.info("Book with id {} assigned to the reader with id {}", id, person.getId());

        return REDIRECT_TO_BOOK.replace("{id}", String.valueOf(id));
    }

    @PatchMapping("/{id}/release")
    public String releaseBookFromTheReader(@PathVariable("id") Long id) {
        log.info("Processing release a book from the reader with id {}", id);
        bookService.updatePersonId(id, null);
        log.info("Book released from the reader with id {} ", id);

        return REDIRECT_TO_BOOK.replace("{id}", String.valueOf(id));
    }

    @GetMapping("/{id}/edit")
    public String getBookUpdatingView(@PathVariable("id") Long id, Model model) {
        log.info("Processing a search book with id {}", id);
        bookService.findById(id).ifPresent(book -> model.addAttribute("book", book));
        return BOOK_UPDATING_VIEW;
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        log.info("Processing update a book with id {}", book.getId());
        if (bindingResult.hasErrors()) {
            brLogger.warn("updateBook", bindingResult);
            return BOOK_UPDATING_VIEW;
        }
        bookService.update(book);
        log.info("Book with id {} updated", book.getId());

        return REDIRECT_TO_BOOK.replace("{id}", String.valueOf(book.getId()));
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        log.info("Processing delete the book by id {}", id);
        bookService.delete(id);
        log.info("Book with id {} deleted", id);

        return REDIRECT_TO_BOOKS;
    }
}

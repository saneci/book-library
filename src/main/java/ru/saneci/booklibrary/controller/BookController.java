package ru.saneci.booklibrary.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import ru.saneci.booklibrary.domain.Book;
import ru.saneci.booklibrary.domain.Person;
import ru.saneci.booklibrary.service.BookService;
import ru.saneci.booklibrary.service.PersonService;
import ru.saneci.booklibrary.util.BindingResultLogger;

@Controller
@RequestMapping("/book")
public class BookController {

    private final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BindingResultLogger brLogger = BindingResultLogger.getLogger(BookController.class);

    private static final String NEW_BOOK_VIEW = "book/new";
    private static final String BOOK_LIST_VIEW = "book/list";
    private static final String BOOK_ITEM_VIEW = "book/item";
    private static final String BOOK_UPDATING_VIEW = "book/update";

    private static final String REDIRECT_TO_BOOK_ALL = "redirect:/book/all";
    private static final String REDIRECT_TO_BOOK_ITEM = "redirect:/book/{id}";

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping("/new")
    public String getNewBookView(@ModelAttribute("book") Book book) {
        return NEW_BOOK_VIEW;
    }

    @PostMapping
    public String addNewBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        log.debug("addNewBook: start processing");
        if (bindingResult.hasErrors()) {
            brLogger.warn("addNewBook", bindingResult);
            return NEW_BOOK_VIEW;
        }
        bookService.save(book);
        log.debug("addNewBook: finish processing");

        return REDIRECT_TO_BOOK_ALL;
    }

    @GetMapping("/all")
    public String getAllBooks(Model model) {
        log.debug("getAllBooks: start processing");
        model.addAttribute("bookList", bookService.findAll());
        log.debug("getAllBooks: finish processing");

        return BOOK_LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") Long id, Model model, @ModelAttribute("person") Person person) {
        log.debug("getBookById: start processing");

        bookService.findBookWithPersonById(id).ifPresent(book -> {
            model.addAttribute("book", book);

            if (book.getPerson() == null) {
                model.addAttribute("people", personService.findAll());
            }
        });
        log.debug("getBookById: finish processing");

        return BOOK_ITEM_VIEW;
    }

    @PatchMapping("/{id}/assign")
    public String assignBookToTheReader(@PathVariable("id") Long id, @ModelAttribute("person") Person person) {
        log.debug("assignBookToTheReader: start processing");
        bookService.updatePersonId(id, person.getId());
        log.debug("assignBookToTheReader: finish processing");

        return REDIRECT_TO_BOOK_ITEM.replace("{id}", String.valueOf(id));
    }

    @PatchMapping("/{id}/release")
    public String releaseBookFromTheReader(@PathVariable("id") Long id) {
        log.debug("releaseBookFromTheReader: start processing");
        bookService.updatePersonId(id, null);
        log.debug("releaseBookFromTheReader: finish processing");

        return REDIRECT_TO_BOOK_ITEM.replace("{id}", String.valueOf(id));
    }

    @GetMapping("/{id}/edit")
    public String getBookUpdatingView(@PathVariable("id") Long id, Model model) {
        log.debug("getBookUpdatingView: start processing");
        bookService.findById(id).ifPresent(book -> model.addAttribute("book", book));
        log.debug("getBookUpdatingView: finish processing");

        return BOOK_UPDATING_VIEW;
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        log.debug("updateBook: start processing");
        if (bindingResult.hasErrors()) {
            brLogger.warn("updateBook", bindingResult);
            return BOOK_UPDATING_VIEW;
        }
        bookService.update(book);
        log.debug("updateBook: finish processing");

        return REDIRECT_TO_BOOK_ALL;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        log.debug("deleteBook: start processing");
        bookService.delete(id);
        log.debug("deleteBook: finish processing");

        return REDIRECT_TO_BOOK_ALL;
    }
}

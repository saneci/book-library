package ru.saneci.booklibrary.controller;

import jakarta.validation.Valid;
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

@Controller
@RequestMapping("/book")
public class BookController {

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
        if (bindingResult.hasErrors()) {
            return NEW_BOOK_VIEW;
        }
        bookService.save(book);
        return REDIRECT_TO_BOOK_ALL;
    }

    @GetMapping("/all")
    public String getAllBooks(Model model) {
        model.addAttribute("bookList", bookService.findAll());
        return BOOK_LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getBookById(@PathVariable("id") Long id, Model model, @ModelAttribute("person") Person person) {
        bookService.findBookWithPersonById(id).ifPresent(book -> {
            model.addAttribute("book", book);

            if (book.getPerson() == null) {
                model.addAttribute("people", personService.findAll());
            }
        });

        return BOOK_ITEM_VIEW;
    }

    @PatchMapping("/{id}/assign")
    public String assignBookToTheReader(@PathVariable("id") Long id, @ModelAttribute("person") Person person) {
        bookService.updatePersonId(id, person.getId());
        return REDIRECT_TO_BOOK_ITEM.replace("{id}", String.valueOf(id));
    }

    @PatchMapping("/{id}/release")
    public String releaseBookFromTheReader(@PathVariable("id") Long id) {
        bookService.updatePersonId(id, null);
        return REDIRECT_TO_BOOK_ITEM.replace("{id}", String.valueOf(id));
    }

    @GetMapping("/{id}/edit")
    public String getBookUpdatingView(@PathVariable("id") Long id, Model model) {
        bookService.findById(id).ifPresent(book -> model.addAttribute("book", book));
        return BOOK_UPDATING_VIEW;
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BOOK_UPDATING_VIEW;
        }
        bookService.update(book);
        return REDIRECT_TO_BOOK_ALL;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.delete(id);
        return REDIRECT_TO_BOOK_ALL;
    }
}

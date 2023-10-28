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
import ru.saneci.booklibrary.dao.BookDAO;
import ru.saneci.booklibrary.dao.PersonDAO;
import ru.saneci.booklibrary.model.Book;
import ru.saneci.booklibrary.model.Person;

@Controller
@RequestMapping("book")
public class BookController {

    private static final String NEW_BOOK_VIEW = "book/new";
    private static final String BOOK_LIST_VIEW = "book/list";
    private static final String BOOK_ITEM_VIEW = "book/item";
    private static final String BOOK_UPDATING_VIEW = "book/update";

    private static final String REDIRECT_TO_BOOK_ALL = "redirect:/book/all";
    private static final String REDIRECT_TO_BOOK_ITEM = "redirect:/book/{id}";

    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
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
        bookDAO.save(book);
        return REDIRECT_TO_BOOK_ALL;
    }

    @GetMapping("/all")
    public String getAllBooks(Model model) {
        model.addAttribute("bookList", bookDAO.findAll());
        return BOOK_LIST_VIEW;
    }

    @GetMapping("/{id}")
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public String getBookById(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        bookDAO.findById(id).ifPresent(book -> {
            model.addAttribute("book", book);

            if (book.getPersonId() == null) {
                model.addAttribute("people", personDAO.findAll());
            } else {
                model.addAttribute("personName", personDAO.findById(book.getPersonId()).get().getName());
            }
        });

        return BOOK_ITEM_VIEW;
    }

    @PatchMapping("{bookId}/assign")
    public String assignBookToTheReader(@PathVariable("bookId") int bookId, @ModelAttribute("person") Person person) {
        bookDAO.updatePersonId(bookId, person.getId());
        return REDIRECT_TO_BOOK_ITEM.replace("{id}", String.valueOf(bookId));
    }

    @PatchMapping("{bookId}/release")
    public String releaseBookFromTheReader(@PathVariable("bookId") int bookId) {
        bookDAO.updatePersonId(bookId, null);
        return REDIRECT_TO_BOOK_ITEM.replace("{id}", String.valueOf(bookId));
    }

    @GetMapping("/{id}/edit")
    public String getBookUpdatingView(@PathVariable("id") int id, Model model) {
        bookDAO.findById(id).ifPresent(book -> model.addAttribute("book", book));
        return BOOK_UPDATING_VIEW;
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return BOOK_UPDATING_VIEW;
        }
        bookDAO.update(book, id);
        return REDIRECT_TO_BOOK_ALL;
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return REDIRECT_TO_BOOK_ALL;
    }
}

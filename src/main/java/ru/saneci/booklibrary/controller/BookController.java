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
import ru.saneci.booklibrary.model.Book;

@Controller
@RequestMapping("book")
public class BookController {

    private static final String NEW_BOOK_VIEW = "book/new";
    private static final String BOOK_LIST_VIEW = "book/list";
    private static final String BOOK_ITEM_VIEW = "book/item";
    private static final String BOOK_UPDATING_VIEW = "book/update";

    private static final String REDIRECT_TO_BOOK_ALL = "redirect:/book/all";

    private final BookDAO bookDAO;

    @Autowired
    public BookController(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
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
    public String getBookById(@PathVariable("id") int id, Model model) {
        bookDAO.findById(id).ifPresent(book -> model.addAttribute("book", book));
        return BOOK_ITEM_VIEW;
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

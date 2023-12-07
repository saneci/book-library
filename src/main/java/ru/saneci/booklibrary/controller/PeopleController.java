package ru.saneci.booklibrary.controller;

import jakarta.validation.Valid;
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
import ru.saneci.booklibrary.model.Person;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private static final String PEOPLE_LIST_VIEW = "people/list";
    private static final String PERSON_VIEW = "people/person";
    private static final String NEW_PEOPLE_VIEW = "people/new";
    private static final String UPDATING_VIEW = "people/update";

    private static final String REDIRECT_TO_PEOPLE = "redirect:/people";

    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    public PeopleController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }

    @GetMapping
    public String getAllReaders(Model model) {
        model.addAttribute("people", personDAO.findAll());
        return PEOPLE_LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getReaderById(@PathVariable("id") int id, Model model) {
        personDAO.findById(id).ifPresent(person -> {
            model.addAttribute("person", person);
            model.addAttribute("bookList", bookDAO.findAllByPersonId(person.getId()));
        });
        return PERSON_VIEW;
    }

    @GetMapping("/new")
    public String getNewPeopleView(@ModelAttribute("person") Person person) {
        return NEW_PEOPLE_VIEW;
    }

    @GetMapping("/{id}/edit")
    public String getUpdatingView(@PathVariable("id") int id, Model model) {
        personDAO.findById(id).ifPresent(person -> model.addAttribute("person", person));
        return UPDATING_VIEW;
    }

    @PostMapping
    public String createNewReader(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return NEW_PEOPLE_VIEW;
        }
        personDAO.save(person);
        return REDIRECT_TO_PEOPLE;
    }

    @PatchMapping("/{id}")
    public String updateReader(@PathVariable("id") int id, @ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return NEW_PEOPLE_VIEW;
        }
        personDAO.update(person, id);
        return REDIRECT_TO_PEOPLE;
    }

    @DeleteMapping("/{id}")
    public String deleteReader(@PathVariable("id") int id) {
        personDAO.delete(id);
        return REDIRECT_TO_PEOPLE;
    }
}

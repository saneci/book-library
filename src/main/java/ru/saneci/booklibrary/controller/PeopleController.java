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
import ru.saneci.booklibrary.domain.Person;
import ru.saneci.booklibrary.service.PersonService;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private static final String PEOPLE_LIST_VIEW = "people/list";
    private static final String PERSON_VIEW = "people/person";
    private static final String NEW_PEOPLE_VIEW = "people/new";
    private static final String UPDATING_VIEW = "people/update";

    private static final String REDIRECT_TO_PEOPLE = "redirect:/people";

    private final PersonService personService;

    public PeopleController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String getAllReaders(Model model) {
        model.addAttribute("people", personService.findAll());
        return PEOPLE_LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getReaderById(@PathVariable("id") Long id, Model model) {
        personService.findPersonWithBooksById(id).ifPresent(person -> model.addAttribute("person", person));
        return PERSON_VIEW;
    }

    @GetMapping("/new")
    public String getNewPeopleView(@ModelAttribute("person") Person person) {
        return NEW_PEOPLE_VIEW;
    }

    @GetMapping("/{id}/edit")
    public String getUpdatingView(@PathVariable("id") Long id, Model model) {
        personService.findById(id).ifPresent(person -> model.addAttribute("person", person));
        return UPDATING_VIEW;
    }

    @PostMapping
    public String createNewReader(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return NEW_PEOPLE_VIEW;
        }
        personService.save(person);
        return REDIRECT_TO_PEOPLE;
    }

    @PatchMapping("/{id}")
    public String updateReader(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return NEW_PEOPLE_VIEW;
        }
        personService.update(person);
        return REDIRECT_TO_PEOPLE;
    }

    @DeleteMapping("/{id}")
    public String deleteReader(@PathVariable("id") Long id) {
        personService.delete(id);
        return REDIRECT_TO_PEOPLE;
    }
}

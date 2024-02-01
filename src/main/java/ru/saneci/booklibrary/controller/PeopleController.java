package ru.saneci.booklibrary.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import ru.saneci.booklibrary.util.BindingResultLogger;

import java.util.Optional;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final Logger log = LoggerFactory.getLogger(PeopleController.class);
    private final BindingResultLogger brLogger = BindingResultLogger.getLogger(PeopleController.class);

    private static final String PEOPLE_LIST_VIEW = "people/list";
    private static final String PERSON_VIEW = "people/person";
    private static final String NEW_PEOPLE_VIEW = "people/new";
    private static final String UPDATING_VIEW = "people/update";

    private static final String REDIRECT_TO_PEOPLE = "redirect:/people";
    private static final String REDIRECT_TO_PERSON = "redirect:/people/{id}";

    private static final String ERROR_404 = "error/not-found";

    private final PersonService personService;

    public PeopleController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public String getAllReaders(Model model) {
        log.debug("getAllReaders: start processing");
        model.addAttribute("people", personService.findAll());
        log.debug("getAllReaders: finish processing");

        return PEOPLE_LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getReaderById(@PathVariable("id") Long id, Model model) {
        log.debug("getReaderById: start processing");
        Optional<Person> person = personService.findPersonWithBooksById(id);

        if (person.isPresent()) {
            model.addAttribute("person", person.get());
            log.debug("getReaderById: finish processing");

            return PERSON_VIEW;
        } else {
            log.debug("getReaderById: finish processing");

            return ERROR_404;
        }
    }

    @GetMapping("/new")
    public String getNewPeopleView(@ModelAttribute("person") Person person) {
        return NEW_PEOPLE_VIEW;
    }

    @GetMapping("/{id}/edit")
    public String getUpdatingView(@PathVariable("id") Long id, Model model) {
        log.debug("getUpdatingView: start processing");
        personService.findById(id).ifPresent(person -> model.addAttribute("person", person));
        log.debug("getUpdatingView: finish processing");

        return UPDATING_VIEW;
    }

    @PostMapping
    public String createNewReader(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        log.debug("createNewReader: start processing");
        if (bindingResult.hasErrors()) {
            brLogger.warn("createNewReader", bindingResult);
            return NEW_PEOPLE_VIEW;
        }
        personService.save(person);
        log.debug("createNewReader: finish processing");

        return REDIRECT_TO_PEOPLE;
    }

    @PatchMapping("/{id}")
    public String updateReader(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult) {
        log.debug("updateReader: start processing");
        if (bindingResult.hasErrors()) {
            brLogger.warn("updateReader", bindingResult);
            return NEW_PEOPLE_VIEW;
        }
        personService.update(person);
        log.debug("updateReader: finish processing");

        return REDIRECT_TO_PERSON.replace("{id}", String.valueOf(person.getId()));
    }

    @DeleteMapping("/{id}")
    public String deleteReader(@PathVariable("id") Long id) {
        log.debug("deleteReader: start processing");
        personService.delete(id);
        log.debug("deleteReader: finish processing");

        return REDIRECT_TO_PEOPLE;
    }
}

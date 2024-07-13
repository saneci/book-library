package ru.saneci.booklibrary.business.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.saneci.booklibrary.business.domain.BusinessPerson;
import ru.saneci.booklibrary.business.service.BusinessPersonService;
import ru.saneci.booklibrary.business.util.BindingResultLogger;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/people")
@RequiredArgsConstructor
public class PeopleController {

    private final BindingResultLogger brLogger = BindingResultLogger.getLogger(PeopleController.class);

    private static final String PEOPLE_LIST_VIEW = "people/list";
    private static final String PERSON_VIEW = "people/person";
    private static final String NEW_PEOPLE_VIEW = "people/new";
    private static final String UPDATING_VIEW = "people/update";

    private static final String REDIRECT_TO_PEOPLE = "redirect:/people";
    private static final String REDIRECT_TO_PERSON = "redirect:/people/{id}";

    private static final String ERROR_404 = "error/not-found";

    private final BusinessPersonService personService;

    @GetMapping
    public String getAllReaders(Model model) {
        log.info("Processing getting all readers");
        model.addAttribute("people", personService.findAll());
        log.info("Found and returned {} readers", personService.findAll().size());

        return PEOPLE_LIST_VIEW;
    }

    @GetMapping("/{id}")
    public String getReaderById(@PathVariable("id") Long id, Model model) {
        log.info("Processing searching for reader with id {}", id);
        Optional<BusinessPerson> person = personService.findPersonWithBooksById(id);

        if (person.isPresent()) {
            model.addAttribute("person", person.get());
            log.info("Reader with id {} found", id);

            return PERSON_VIEW;
        } else {
            log.info("Reader with id {} not found", id);

            return ERROR_404;
        }
    }

    @GetMapping("/new")
    public String getNewPeopleView(@ModelAttribute("person") BusinessPerson person) {
        log.info("Returning new reader registration page");
        return NEW_PEOPLE_VIEW;
    }

    @GetMapping("/{id}/edit")
    public String getUpdatingView(@PathVariable("id") Long id, Model model) {
        log.info("Processing searching for reader with id {} to update", id);
        personService.findById(id).ifPresent(person -> model.addAttribute("person", person));
        log.info("Reader with id {} found and will be updated", id);

        return UPDATING_VIEW;
    }

    @PostMapping
    public String createNewReader(@ModelAttribute("person") @Valid BusinessPerson person, BindingResult bindingResult) {
        log.info("Processing create new reader with id {}", person.getId());
        if (bindingResult.hasErrors()) {
            brLogger.warn("createNewReader", bindingResult);
            return NEW_PEOPLE_VIEW;
        }

        personService.save(person);
        log.info("New reader with id {} created", person.getId());

        return REDIRECT_TO_PEOPLE;
    }

    @PatchMapping("/{id}")
    public String updateReader(@ModelAttribute("person") @Valid BusinessPerson person,
                               BindingResult bindingResult) {
        log.info("Processing update reader with id {}", person.getId());
        if (bindingResult.hasErrors()) {
            brLogger.warn("updateReader", bindingResult);
            return NEW_PEOPLE_VIEW;
        }

        personService.update(person);
        log.info("Reader with id {} updated", person.getId());

        return REDIRECT_TO_PERSON.replace("{id}", String.valueOf(person.getId()));
    }

    @DeleteMapping("/{id}")
    public String deleteReader(@PathVariable("id") Long id) {
        log.info("Processing delete reader with id {}", id);
        personService.delete(id);
        log.info("Reader with id {} deleted", id);

        return REDIRECT_TO_PEOPLE;
    }
}

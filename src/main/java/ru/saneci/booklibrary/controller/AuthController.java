package ru.saneci.booklibrary.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.saneci.booklibrary.domain.Person;
import ru.saneci.booklibrary.domain.Role;
import ru.saneci.booklibrary.service.PersonService;
import ru.saneci.booklibrary.util.BindingResultLogger;
import ru.saneci.booklibrary.util.PersonValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final BindingResultLogger brLogger = BindingResultLogger.getLogger(AuthController.class);
    private final PersonValidator personValidator;
    private final PersonService personService;

    @Autowired
    public AuthController(PersonValidator personValidator, PersonService personService) {
        this.personValidator = personValidator;
        this.personService = personService;
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("person") Person person) {
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        log.debug("performRegistration: start processing");
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            brLogger.warn("performRegistration", bindingResult);
            return "auth/registration";
        }
        // TODO: remove setRole after https://github.com/users/saneci/projects/3/views/1?pane=issue&itemId=56174894
        person.setRole(Role.ROLE_USER);
        personService.save(person);
        log.debug("performRegistration: finish processing");

        return "redirect:/auth/login";
    }
}

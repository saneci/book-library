package ru.saneci.booklibrary.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.saneci.booklibrary.security.domain.SecurityPerson;
import ru.saneci.booklibrary.security.service.SecurityPersonService;
import ru.saneci.booklibrary.security.util.BindingResultLogger;
import ru.saneci.booklibrary.security.util.PersonValidator;

@Slf4j
@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final BindingResultLogger brLogger = BindingResultLogger.getLogger(AuthController.class);
    private final PersonValidator personValidator;
    private final SecurityPersonService personService;

    @GetMapping("/login")
    public String getLoginPage() {
        log.info("Returning user login page");
        return "auth/login";
    }

    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("person") SecurityPerson person) {
        log.info("Returning user registration page");
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("person") @Valid SecurityPerson person, BindingResult bindingResult) {
        log.info("Processing registration user with username {}", person.getUsername());
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            brLogger.warn("performRegistration", bindingResult);
            return "auth/registration";
        }

        personService.createNewUser(person);
        log.info("User with username {} registered and will be redirected to login page", person.getUsername());

        return "redirect:/auth/login";
    }
}

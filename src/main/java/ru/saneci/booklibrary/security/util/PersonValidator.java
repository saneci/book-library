package ru.saneci.booklibrary.security.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.saneci.booklibrary.security.domain.SecurityPerson;
import ru.saneci.booklibrary.security.service.SecurityPersonService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PersonValidator implements Validator {

    private final SecurityPersonService personService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SecurityPerson.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("Validate person");
        SecurityPerson person = (SecurityPerson) target;

        if (personService.findByUserName(person.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "Пользователь с таким логином уже существует");
        }

        if (!person.getPassword().equals(person.getPasswordRepeat())) {
            errors.rejectValue("passwordRepeat", "", "Пароли не совпадают");
        }
    }
}

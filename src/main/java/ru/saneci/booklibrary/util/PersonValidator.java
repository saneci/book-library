package ru.saneci.booklibrary.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.saneci.booklibrary.domain.Person;
import ru.saneci.booklibrary.service.PersonService;

@Component
public class PersonValidator implements Validator {

    private final PersonService peopleService;

    @Autowired
    public PersonValidator(PersonService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;

        if (peopleService.findByUserName(person.getUsername()).isPresent()) {
            errors.rejectValue("username", "", "Пользователь с таким логином уже существует");
        }

        if (!person.getPassword().equals(person.getPasswordRepeat())) {
            errors.rejectValue("passwordRepeat", "", "Пароли не совпадают");
        }
    }
}

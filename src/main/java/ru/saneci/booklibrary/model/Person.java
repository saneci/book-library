package ru.saneci.booklibrary.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class Person {

    private int id;

    @Pattern(regexp = "(([A-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+)|([A-Z]\\w+ [A-Z]\\w+( [A-Z]\\w+)?))",
            message = "ФИО должно быть в следующем формате: Имя Фамилия Отчество")
    private String name;

    @Positive
    @Min(value = 1900, message = "Год рождения должен быть больше чем 1899")
    private int birthdayYear;

    public Person() {
    }

    public Person(int id, String name, int birthdayYear) {
        this.id = id;
        this.name = name;
        this.birthdayYear = birthdayYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthdayYear() {
        return birthdayYear;
    }

    public void setBirthdayYear(int birthdayYear) {
        this.birthdayYear = birthdayYear;
    }
}

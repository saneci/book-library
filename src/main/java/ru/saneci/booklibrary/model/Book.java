package ru.saneci.booklibrary.model;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public class Book {

    private int id;

    private Integer personId;

    @Pattern(regexp = "([А-Я]|[A-Z]).{1,100}",
            message = "Название книги должно начинаться с заглавной буквы и содержать от 2 до 100 символов")
    private String title;

    @Pattern(regexp = "(([A-Я][а-я]+ [А-Я][а-я]+( [А-Я][а-я]+)?)|([A-Z]\\w+ [A-Z]\\w+( [A-Z]\\w+)?))",
            message = "ФИО автора должно быть в следующем формате: Имя Фамилия Отчество")
    private String author;

    @Positive(message = "Год публикации должен быть больше 0")
    private int publishYear;

    public Book() {
    }

    public Book(int id, int personId, String title, String author, int publishYear) {
        this.id = id;
        this.personId = personId;
        this.title = title;
        this.author = author;
        this.publishYear = publishYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }
}

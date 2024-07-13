package ru.saneci.booklibrary.business.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "([А-ЯA-Z]).{1,100}",
            message = "Название книги должно начинаться с заглавной буквы и содержать от 2 до 100 символов")
    @Column(name = "title")
    private String title;

    @Pattern(regexp = "(([A-Я][а-я]+ [А-Я][а-я]+( [А-Я][а-я]+)?)|([A-Z]\\w+ [A-Z]\\w+( [A-Z]\\w+)?))",
            message = "ФИО автора должно быть в следующем формате: Имя Фамилия Отчество")
    @Column(name = "author")
    private String author;

    @Positive(message = "Год публикации должен быть больше 0")
    @Column(name = "publish_year")
    private int publishYear;

    @Column(name = "given_at")
    private LocalDateTime givenAt;

    @Transient
    private boolean isOverdue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private BusinessPerson person;
}

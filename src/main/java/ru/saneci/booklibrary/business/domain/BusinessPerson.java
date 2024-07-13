package ru.saneci.booklibrary.business.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "person")
public class BusinessPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Pattern(regexp = "(([A-Я][а-я]+ [А-Я][а-я]+ [А-Я][а-я]+)|([A-Z]\\w+ [A-Z]\\w+( [A-Z]\\w+)?))",
            message = "ФИО должно быть в следующем формате: Имя Фамилия Отчество")
    @Column(name = "name")
    private String name;

    @Min(value = 1900, message = "Год рождения должен быть больше чем 1899")
    @Column(name = "birthday_year")
    private int birthdayYear;

    @Column(name = "role")
    private String role = "ROLE_READER";

    @OneToMany(mappedBy = "person")
    @Cascade(CascadeType.PERSIST)
    private List<Book> bookList;
}

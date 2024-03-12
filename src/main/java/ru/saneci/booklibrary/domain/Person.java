package ru.saneci.booklibrary.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Entity
@Table(name = "person")
public class Person {

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

    @Column(name = "username")
    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "[^а-яА-Я]*", message = "Логин не должен содержать кириллицу")
    private String username;

    @Column(name = "password")
    @Length(min = 6, message = "Пароль должен содержать минимум 6 символов")
    @Pattern(regexp = "[^а-яА-Я]*", message = "Пароль не должен содержать кириллицу")
    private String password;

    @Transient
    private String passwordRepeat;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "person")
    @Cascade(CascadeType.PERSIST)
    private List<Book> bookList;

    public Person() {
    }

    public Person(String name, int birthdayYear) {
        this.name = name;
        this.birthdayYear = birthdayYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}

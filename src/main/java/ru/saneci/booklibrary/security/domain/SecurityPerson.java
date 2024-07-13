package ru.saneci.booklibrary.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@Entity
@Table(name = "person")
public class SecurityPerson {

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
    private Role role = Role.ROLE_USER;
}

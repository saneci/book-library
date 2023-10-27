package ru.saneci.booklibrary.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.saneci.booklibrary.model.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> findAll() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Optional<Person> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id = ?", new BeanPropertyRowMapper<>(Person.class), id)
                .stream().findAny();
    }

    public void save(Person person) {
        String sql = "INSERT INTO person (name, birthday_year) VALUES (?, ?)";
        jdbcTemplate.update(sql, person.getName(), person.getBirthdayYear());
    }

    public void update(Person person, int id) {
        String sql = "UPDATE person SET name = ?, birthday_year = ? WHERE id = ?";
        jdbcTemplate.update(sql, person.getName(), person.getBirthdayYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id = ?", id);
    }
}

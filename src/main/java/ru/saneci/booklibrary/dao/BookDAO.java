package ru.saneci.booklibrary.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.saneci.booklibrary.model.Book;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public List<Book> findAllByPersonId(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE person_id = ?", new BeanPropertyRowMapper<>(Book.class), id);
    }

    public Optional<Book> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE id = ?", new BeanPropertyRowMapper<>(Book.class), id)
                .stream().findAny();
    }

    public void save(Book book) {
        String sql = "INSERT INTO book (person_id, title, author, publish_year) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, book.getPersonId(), book.getTitle(), book.getAuthor(), book.getPublishYear());
    }

    public void update(Book book, int bookId) {
        String sql = "UPDATE book SET person_id=?, title=?, author=?, publish_year=? WHERE id=?";
        jdbcTemplate.update(sql, book.getPersonId(), book.getTitle(), book.getAuthor(), book.getPublishYear(), bookId);
    }

    public void updatePersonId(int bookId, Integer personId) {
        String sql = "UPDATE book SET person_id=? WHERE id=?";
        jdbcTemplate.update(sql, personId, bookId);
    }

    public void delete(int bookId) {
        jdbcTemplate.update("DELETE FROM book WHERE id = ?", bookId);
    }
}

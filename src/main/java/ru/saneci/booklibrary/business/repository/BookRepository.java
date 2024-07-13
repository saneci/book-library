package ru.saneci.booklibrary.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.saneci.booklibrary.business.domain.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b left join fetch b.person where b.id = ?1")
    Optional<Book> findBookWithPersonById(Long id);

    List<Book> findAllByTitleContainsIgnoreCase(String title);
}

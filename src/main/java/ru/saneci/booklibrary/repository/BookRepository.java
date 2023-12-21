package ru.saneci.booklibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.saneci.booklibrary.domain.Book;
import ru.saneci.booklibrary.domain.Person;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("select b from Book b left join fetch b.person where b.id = ?1")
    Optional<Book> findBookWithPersonById(Long id);

    @Modifying
    @Query("update Book b set b.person = ?1 where b.id = ?2")
    int updatePersonId(Person person, Long bookId);
}

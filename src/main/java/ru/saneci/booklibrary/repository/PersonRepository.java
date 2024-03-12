package ru.saneci.booklibrary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.saneci.booklibrary.domain.Person;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("select p from Person p left join fetch p.bookList where p.id = ?1")
    Optional<Person> findPersonWithBooksById(Long id);

    Optional<Person> findPersonByUsername(String username);
}

package ru.saneci.booklibrary.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.saneci.booklibrary.business.domain.BusinessPerson;

import java.util.Optional;

@Repository
public interface BusinessPersonRepository extends JpaRepository<BusinessPerson, Long> {

    @Query("select p from BusinessPerson p left join fetch p.bookList where p.id = ?1")
    Optional<BusinessPerson> findPersonWithBooksById(Long id);
}

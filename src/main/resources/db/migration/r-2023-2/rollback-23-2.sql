ALTER TABLE book
DROP CONSTRAINT book_person_id_fkey,
ADD CONSTRAINT book_person_id_fkey
    FOREIGN KEY (person_id)
    REFERENCES person (id)
    ON DELETE CASCADE;
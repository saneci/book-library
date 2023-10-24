CREATE TABLE person
(
    id            int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name          varchar(100) NOT NULL,
    birthday_year int          NOT NULL,
    UNIQUE (name, birthday_year)
);

CREATE TABLE book
(
    id           int GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    person_id    int REFERENCES person (id) ON DELETE CASCADE,
    title        varchar(100) NOT NULL,
    author       varchar(100) NOT NULL,
    publish_year int          NOT NULL,
    UNIQUE (title, author, publish_year)
);

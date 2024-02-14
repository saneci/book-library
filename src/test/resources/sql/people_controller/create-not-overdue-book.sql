truncate book, person;

insert into person (id, name, birthday_year) values (1, 'Test User Name', 2020);

insert into book (id, person_id, title, author, publish_year, given_at)
values (1, 1, 'Test Book', 'Test Author', 1234, now() - interval '9 days');
truncate book, person;

insert into person (id, name, birthday_year, username, password, role)
values (1, 'Test User Name', 2020, 'username', 'password', 'ROLE_READER');

insert into book (id, person_id, title, author, publish_year)
values (1, 1, 'Test Book', 'Test Author', 1234);
truncate person cascade;

insert into person (id, name, birthday_year, username, password, role)
values (1, 'Test User', 1990, 'username', 'password', 'ROLE_READER');
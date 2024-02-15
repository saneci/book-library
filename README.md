## Books registry

Digital accounting of books in the library

### Table of content

1. [Business problem](#business-problem)
2. [Technologies used in the project](#technologies-used-in-the-project)
3. [Connecting to the database](#connecting-to-the-database)
4. [Running application](#running-application)

#### Business problem

The library needs to move to registering books in digital format. Librarians must be able to register readers, 
check out books, and release books (after the reader returns the book back to the library). 
It is necessary to implement a web application for this purpose.

#### Technologies used in the project

- Java 17
- Spring Boot 3.2.2
- Spring Web MVC
- Spring Data JPA
- Hibernate
- PostgreSQL 16
- Thymeleaf
- Apache Tomcat

#### Connecting to the database

1. Install the DBMS [PostgreSQL](https://www.postgresql.org/download/)
2. Using PG Admin, create a new database and then connect to the DBMS
3. Execute DDL scripts located in the folder `src/main/resources/db/migration`

#### Running application

1. Upload jar file from [latest release](https://github.com/saneci/book-library/releases/latest)
2. Run the application using below command (don't forget substitute right values instead of variables)
    ```shell
    java -Dspring.datasource.url=jdbc:postgresql://[database_host]:[database_port]/[database_name] \ 
	     -Dspring.datasource.username=[database_username] \
	     -Dspring.datasource.password=[database_password] \
	     -Dspring.application.days_until_book_overdue=[int_value] \
	     -jar target/[jar_file_name]
    ```
   The application will be launched at [localhost:8080](http://localhost:8080)

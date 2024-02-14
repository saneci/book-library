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
- Spring Framework 6.0
- PostgreSQL 16
- Thymeleaf 3.1
- Apache Tomcat 10

#### Connecting to the database

1. Install the DBMS [PostgreSQL](https://www.postgresql.org/download/)
2. Using PG Admin, create a new database and then connect to the DBMS
3. Execute DDL scripts located in the folder `src/main/resources/db/migration`
4. Copy configuration file `src/main/resources/application.properties.origin` into `src/main/resources`,
   and rename it to `application.properties`
5. Specify data for connecting to the Database in the created configuration file

#### Running application

1. Install [Tomcat](https://tomcat.apache.org/download-10.cgi) 
2. Configure the IDE to work with Tomcat ([instructions](https://www.jetbrains.com/idea/guide/tutorials/working-with-apache-tomcat/using-existing-application/))
3. Start Tomcat
4. The application will be launched at [localhost:8080](http://localhost:8080)

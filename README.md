## Book Library

Учебный проект из курса на Swiftbook.org (Spring - Полный курс. Boot, Hibernate, Security, REST)

### Содержание

1. [Бизнес задача](#бизнес-задача)
2. [Технологии, используемые в проекте](#технологии-и-инструменты-используемые-в-проекте)
3. [Подключение к базе данных](#подключение-к-базе-данных)
4. [Запуск приложения](#запуск-приложения)

#### Бизнес задача

В местной библиотеке хотят перейти на цифровой учет книг. Необходимо реализовать веб-приложение для них.
Библиотекари должны иметь возможность регистрировать читателей, выдавать им книги и освобождать книги
(после того, как читатель возвращает книгу обратно в библиотеку).

#### Технологии и инструменты, используемые в проекте

- Java 17
- Spring Framework 6.0
- PostgreSQL 16
- Thymeleaf 3.1
- Apache Tomcat 10

#### Подключение к базе данных

1. Установить СУБД [PostgreSQL](https://www.postgresql.org/download/)
2. С помощью PG Admin создать новую БД, после чего подключиться к ней
3. Выполнить DDL скрипты, расположенные в файле `src/main/resources/db/migration/r-2023-1/deploy.sql`
4. Скопировать конфигурационный файл `src/main/resources/application.properties.origin` в `src/main/resources`,
   переименовав его в `application.properties`
5. Указать в конфигурационном файле данные для подключения к БД

#### Запуск приложения

1. Установить [Tomcat](https://tomcat.apache.org/download-10.cgi) 
2. Настроить IDE для работы с Tomcat ([инструкция](https://www.jetbrains.com/idea/guide/tutorials/working-with-apache-tomcat/using-existing-application/))
3. Запустить Tomcat
4. Перейти на главную страницу приложения по адресу [localhost:8080](http://localhost:8080)

    ![alt text](https://github.com/saneci/book-library/blob/master/src/main/resources/img/main_page.jpg?raw=true)
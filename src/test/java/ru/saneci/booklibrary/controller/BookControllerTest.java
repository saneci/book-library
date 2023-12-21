package ru.saneci.booklibrary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.saneci.booklibrary.BaseControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = "/sql/book_controller/create-book.sql")
class BookControllerTest extends BaseControllerTest {

    @Test
    void whenGetNewBookView_thenNewBookViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/book/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/new"))
                .andExpect(xpath("/html/head/title").string("Добавление новой книги"))
                .andExpect(xpath("/html/body/h3").string("Добавление новой книги"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[1]").string("На главную"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[2]").string("К списку книг"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/truncate-book.sql")
    void whenAddNewBook_thenRedirectToBookAllEndpoint() throws Exception {
        MockHttpServletRequestBuilder addNewBook = post("/book")
                .param("title", "Title")
                .param("author", "Some Test Author")
                .param("publishYear", "1999");

        mockMvc.perform(addNewBook)
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/all"));
    }

    @Test
    void whenGetAllBooks_thenBookListViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/book/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("book/list"))
                .andExpect(xpath("/html/head/title").string("Список книг"))
                .andExpect(xpath("/html/body/h3").string("Список книг"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[1]").string("На главную"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[2]").string("Добавить новую книгу"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-three-books.sql")
    void whenGetAllBooksReturnThreeItems_thenBookListViewShouldContainsThreeLinksInTheCorrespondingDivBlock() throws Exception {
        mockMvc.perform(get("/book/all"))
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[@id='book-list']").nodeCount(3));
    }

    @Test
    void whenGetBookById_thenBookItemViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/book/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("book/item"))
                .andExpect(xpath("/html/head/title").string("Карточка книги Test Book"))
                .andExpect(xpath("/html/body/h3").string("Карточка книги"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[1]").string("На главную"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[2]").string("К списку книг"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenAssignBookToTheReader_thenRedirectToGetBookByIdEndpoint() throws Exception {
        mockMvc.perform(patch("/book/{id}/assign", 1))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenAssignBookToTheReader_thenBookShouldBeLinkedToThePerson() throws Exception {
        mockMvc.perform(patch("/book/{id}/assign", 1));

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div[@id='person-books']/ul/li")
                        .string("Test Book, Test Author, 1234"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenReleaseBookFromTheReader_thenRedirectToGetBookByIdEndpoint() throws Exception {
        mockMvc.perform(patch("/book/{id}/release", 1))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenReleaseBookFromTheReader_thenBookShouldBeUnlinkedToThePerson() throws Exception {
        mockMvc.perform(patch("/book/{id}/release", 1));

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/p[2]")
                        .string("Человек пока не взял не одной книги"));
    }

    @Test
    void whenGetBookUpdatingView_thenUpdateViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/book/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("book/update"))
                .andExpect(xpath("/html/head/title").string("Обновление карточки книги Test Book"))
                .andExpect(xpath("/html/body/h3").string("Обновление карточки книги"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenUpdateBook_thenRedirectToBookAllEndpoint() throws Exception {
        MockHttpServletRequestBuilder updateBook = patch("/book/{id}", 1)
                .param("title", "Test Book Updated")
                .param("author", "Test Author Updated")
                .param("publishYear", "2000");

        mockMvc.perform(updateBook)
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/all"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenUpdateBook_thenBookShouldBeUpdated() throws Exception {
        MockHttpServletRequestBuilder updateBook = patch("/book/{id}", 1)
                .param("title", "Test Book Updated")
                .param("author", "Test Author Updated")
                .param("publishYear", "2000");

        mockMvc.perform(updateBook);

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/ul/li")
                        .string("Test Book Updated, Test Author Updated, 2000"));
    }

    @Test
    void whenDeleteBook_thenRedirectToBookAllEndpoint() throws Exception {
        mockMvc.perform(delete("/book/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/all"));
    }
}

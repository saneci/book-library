package ru.saneci.booklibrary.controller;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.saneci.booklibrary.BaseControllerTest;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@Sql(scripts = "/sql/book_controller/create-book.sql", executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/book_controller/truncate-book.sql", executionPhase = AFTER_TEST_METHOD)
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
    void whenAddNewBook_thenRedirectToBookAllEndpoint() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = post("/book")
                .param("title", "Title")
                .param("author", "Author")
                .param("publishYear", "1999");

        mockMvc.perform(requestBuilder)
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
    @Sql(scripts = "/sql/book_controller/create-three-book.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/book_controller/truncate-book.sql", executionPhase = AFTER_TEST_METHOD)
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
    @Sql(scripts = "/sql/people_controller/create-person.sql", executionPhase = BEFORE_TEST_METHOD)
    @Sql(scripts = "/sql/people_controller/truncate-person.sql", executionPhase = AFTER_TEST_METHOD)
    void whenAssignBookToTheReader_thenRedirectToGetBookByIdEndpoint() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = patch("/book/{id}/assign", 1)
                .param("id", "1"); // personId

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1"));
    }

    @Test
    void whenReleaseBookFromTheReader_thenRedirectToGetBookByIdEndpoint() throws Exception {
        mockMvc.perform(patch("/book/{id}/release", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/1"));
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
    void whenUpdateBook_thenRedirectToBookAllEndpoint() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = patch("/book/{id}", 1)
                .param("title", "Title Updated")
                .param("author", "Author Updated")
                .param("publishYear", "2000");

        mockMvc.perform(requestBuilder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/all"));
    }

    @Test
    void whenDeleteBook_thenRedirectToBookAllEndpoint() throws Exception {
        mockMvc.perform(delete("/book/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/book/all"));
    }
}

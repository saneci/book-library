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
        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/new"))
                .andExpect(xpath("/html/head/title").string("Добавление новой книги - SB Library"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[1]/a/span").string("Главная"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[2]/a").string("Список книг"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[3]").string("Добавление новой книги"))
                .andExpect(xpath("//*[@id='mainContent']/div/form[@method='POST']").exists())
                .andExpect(xpath("//*[@id='mainContent']/div/form/div").nodeCount(3));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/truncate-book.sql")
    void whenAddNewBook_thenRedirectToBookAllEndpoint() throws Exception {
        MockHttpServletRequestBuilder addNewBook = post("/books")
                .param("title", "Title")
                .param("author", "Some Test Author")
                .param("publishYear", "1999");

        mockMvc.perform(addNewBook)
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    void whenGetAllBooks_thenBookListViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/list"))
                .andExpect(xpath("/html/head/title").string("Список книг - SB Library"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[1]/a/span").string("Главная"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[2]").string("Список книг"))
                .andExpect(xpath("//*[@id='buttonBlock']/div/ul/li[1]/span").string("Строк в таблице"))
                .andExpect(xpath("//*[@id='buttonBlock']/div/ul/li[2]/a").number(5d))
                .andExpect(xpath("//*[@id='buttonBlock']/div/ul/li[3]/a").number(10d))
                .andExpect(xpath("//*[@id='buttonBlock']/div/ul/li[4]/a").number(20d))
                .andExpect(xpath("//*[@id='buttonBlock']/a").string("Добавить новую книгу"))
                .andExpect(xpath("//*[@id='bookList']/table").exists());
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-three-books.sql")
    void whenGetAllBooksReturnThreeItems_thenBookListViewShouldContainThreeRowsInTheTable() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='bookList']/table/tbody/tr").nodeCount(3));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-twelve-books.sql")
    void whenGetAllBooksWithoutRequestParams_thenReturnDataBasedOnDefaultSizeParameter() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpectAll(
                        xpath("//*[@id='bookList']/table/tbody/tr").nodeCount(10),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='firstPage']/a").exists(),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='previousPage']/a").exists(),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber0']/a").number(1d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber1']/a").number(2d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber2']/a").doesNotExist(),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='nextPage']/a").exists(),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='lastPage']/a").exists()
                );
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-twelve-books.sql")
    void whenGetAllBooksWithPageParam_thenReturnCorrespondingPage() throws Exception {
        mockMvc.perform(get("/books").queryParam("page", "1"))
                .andExpect(status().isOk())
                .andExpectAll(
                        xpath("//*[@id='bookList']/table/tbody/tr").nodeCount(2),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber0']/a").number(1d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber1']/a").number(2d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber2']/a").doesNotExist()
                );
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-twelve-books.sql")
    void whenGetAllBooksWithSizeParamLessThanMaxRows_thenReturnCorrespondingPageWithPaginationButtonBlock() throws Exception {
        mockMvc.perform(get("/books").queryParam("size", "11"))
                .andExpect(status().isOk())
                .andExpectAll(
                        xpath("//*[@id='bookList']/table/tbody/tr").nodeCount(11),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber0']/a").number(1d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber1']/a").number(2d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber2']/a").doesNotExist()
                );
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-twelve-books.sql")
    void whenGetAllBooksWithSizeParamGreaterThanOrEqualsMaxRows_thenReturnCorrespondingPageWithoutPaginationButtonBlock() throws Exception {
        mockMvc.perform(get("/books").queryParam("size", "12"))
                .andExpect(status().isOk())
                .andExpectAll(
                        xpath("//*[@id='bookList']/table/tbody/tr").nodeCount(12),
                        xpath("//*[@id='tableNavigation']").doesNotExist()
                );
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-twelve-books.sql")
    void whenGetAllBooksWithPageAndSizeParams_thenReturnCorrespondingPageAndPaginationButtonBlock() throws Exception {
        mockMvc.perform(get("/books").queryParam("size", "5").queryParam("page", "1"))
                .andExpect(status().isOk())
                .andExpectAll(
                        xpath("//*[@id='bookList']/table/tbody/tr").nodeCount(5),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber0']/a").number(1d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber1']/a").number(2d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber2']/a").number(3d),
                        xpath("//*[@id='tableNavigation']/ul/li[@id='pageNumber3']/a").doesNotExist()
                );
    }

    @Test
    void whenGetBookById_thenBookItemViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/books/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book"))
                .andExpect(xpath("/html/head/title").string("Карточка книги Test Book - SB Library"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[1]/a/span").string("Главная"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[2]/a").string("Список книг"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[3]").string("Карточка книги"))
                .andExpect(xpath("//*[@id='bookInfo']/div[@class='card col-lg-9']/div").nodeCount(2))
                .andExpect(xpath("//*[@id='bookInfo']/div[@id='deleteConfirmationModal']").exists());
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenAssignBookToTheReader_thenRedirectToGetBookByIdEndpoint() throws Exception {
        mockMvc.perform(patch("/books/{id}/assign", 1))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/1"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenAssignBookToTheReader_thenBookShouldBeLinkedToThePerson() throws Exception {
        mockMvc.perform(patch("/books/{id}/assign", 1));

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='personInfo']/div/div/div/ol/li/a")
                        .string("Test Book, Test Author, 1234"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenReleaseBookFromTheReader_thenRedirectToGetBookByIdEndpoint() throws Exception {
        mockMvc.perform(patch("/books/{id}/release", 1))
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/1"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenReleaseBookFromTheReader_thenBookShouldBeUnlinkedToThePerson() throws Exception {
        mockMvc.perform(patch("/books/{id}/release", 1));

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='personInfo']/div/div/div/p")
                        .string("Человек пока не взял не одной книги"));
    }

    @Test
    void whenGetBookUpdatingView_thenUpdateViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/books/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("books/update"))
                .andExpect(xpath("/html/head/title").string("Обновление данных книги Test Book - SB Library"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[1]/a/span").string("Главная"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[2]/a").string("Список книг"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[3]/a").string("Карточка книги"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[4]").string("Обновление данных книги"))
                .andExpect(xpath("//*[@id='mainContent']/div/form/input[@name='_method'][@value='PATCH']").exists())
                .andExpect(xpath("//*[@id='mainContent']/div/form/div").nodeCount(3));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenUpdateBook_thenRedirectToBookAllEndpoint() throws Exception {
        MockHttpServletRequestBuilder updateBook = patch("/books/{id}", 1)
                .param("title", "Test Book Updated")
                .param("author", "Test Author Updated")
                .param("publishYear", "2000");

        mockMvc.perform(updateBook)
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/1"));
    }

    @Test
    @Sql(scripts = "/sql/book_controller/create-book-with-linked-person.sql")
    void whenUpdateBook_thenBookShouldBeUpdated() throws Exception {
        MockHttpServletRequestBuilder updateBook = patch("/books/{id}", 1)
                .param("title", "Test Book Updated")
                .param("author", "Test Author Updated")
                .param("publishYear", "2000");

        mockMvc.perform(updateBook);

        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='personInfo']/div/div/div/ol/li/a")
                        .string("Test Book Updated, Test Author Updated, 2000"));
    }

    @Test
    void whenDeleteBook_thenRedirectToBookAllEndpoint() throws Exception {
        mockMvc.perform(delete("/books/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));
    }
}

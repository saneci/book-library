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

@Sql(scripts = "/sql/people_controller/create-person.sql")
class PeopleControllerTest extends BaseControllerTest {

    @Test
    void whenGetAllReaders_thenPeopleViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/list"))
                .andExpect(xpath("/html/head/title").string("Список читателей"))
                .andExpect(xpath("/html/body/h3").string("Список читателей"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[1]").string("На главную"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[2]").string("Добавить в базу данных нового читателя"));
    }

    @Test
    void whenGetReaderById_thenPersonViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("people/person"))
                .andExpect(xpath("/html/head/title").string("Карточка читателя Test User"))
                .andExpect(xpath("/html/body/h3").string("Карточка читателя"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[1]").string("На главную"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[2]").string("К списку читателей"));
    }

    @Test
    void whenGetReaderByIdModelAttributeBookListIsEmpty_thenViewPersonShouldContainAppropriateMessage() throws Exception {
        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/p[1]").string("Test User, 1990г.р."))
                .andExpect(xpath("/html/body/p[2]").string("Человек пока не взял не одной книги"));
    }

    @Test
    void whenGetNewPeopleView_thenNewViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/people/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/new"))
                .andExpect(xpath("/html/head/title").string("Добавление нового читателя"))
                .andExpect(xpath("/html/body/h3").string("Добавление нового читателя"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[1]").string("На главную"))
                .andExpect(xpath("/html/body/div[@id='footer']/a[2]").string("К списку читателей"));
    }

    @Test
    void whenGetUpdatingView_thenUpdateViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/people/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("people/update"))
                .andExpect(xpath("/html/head/title").string("Обновление карточки читателя Test User"))
                .andExpect(xpath("/html/body/h3").string("Обновление карточки читателя"));
    }

    @Test
    @Sql(scripts = "/sql/people_controller/truncate-person.sql")
    void whenCreateNewReader_thenRedirectToPeopleEndpoint() throws Exception {
        MockHttpServletRequestBuilder postRequest = post("/people")
                .param("name", "Test User Name")
                .param("birthdayYear", "1900");

        mockMvc.perform(postRequest)
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/people"));

    }

    @Test
    void whenUpdateReader_thenRedirectToPeopleEndpoint() throws Exception {
        MockHttpServletRequestBuilder updateReader = patch("/people/{id}", 1)
                .param("name", "Test User Updated")
                .param("birthdayYear", "12345");

        mockMvc.perform(updateReader)
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/people"));
    }

    @Test
    void whenDeleteReader_thenRedirectToPeopleEndpoint() throws Exception {
        mockMvc.perform(delete("/people/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/people"));
    }
}
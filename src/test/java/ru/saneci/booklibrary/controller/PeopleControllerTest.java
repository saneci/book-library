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
                .andExpect(xpath("/html/head/title").string("Список читателей - SB Library"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[1]/a/span").string("Главная"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[2]").string("Список читателей"))
                .andExpect(xpath("//*[@id='peopleList']/div/a").string("Добавить нового читателя"))
                .andExpect(xpath("//*[@id='peopleList']/table").exists());
    }

    @Test
    void whenGetReaderById_thenPersonViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("people/person"))
                .andExpect(xpath("/html/head/title").string("Карточка читателя Test User - SB Library"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[1]/a/span").string("Главная"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[2]/a").string("Список читателей"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[3]").string("Карточка читателя"))
                .andExpect(xpath("//*[@id='personInfo']/div[@class='card col-lg-9']/div").nodeCount(2))
                .andExpect(xpath("//*[@id='personInfo']/div[@id='deleteConfirmationModal']").exists());
    }

    @Test
    void whenGetReaderByIdModelAttributeBookListIsEmpty_thenViewPersonShouldContainAppropriateMessage() throws Exception {
        mockMvc.perform(get("/people/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(xpath("//*[@id='personInfo']/div/div/h5").string("Test User, 1990г.р."))
                .andExpect(xpath("//*[@id='personInfo']/div/div/div/p").string("Человек пока не взял не одной книги"));
    }

    @Test
    void whenGetNewPeopleView_thenNewViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/people/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("people/new"))
                .andExpect(xpath("/html/head/title").string("Добавление нового читателя - SB Library"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[1]/a/span").string("Главная"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[2]/a").string("Список читателей"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[3]").string("Добавление нового читателя"))
                .andExpect(xpath("//*[@id='mainContent']/div/form[@method='POST']").exists())
                .andExpect(xpath("//*[@id='mainContent']/div/form/div").nodeCount(2));
    }

    @Test
    void whenGetUpdatingView_thenUpdateViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/people/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("people/update"))
                .andExpect(xpath("/html/head/title").string("Обновление данных читателя Test User - SB Library"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[1]/a/span").string("Главная"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[2]/a").string("Список читателей"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[3]/a").string("Карточка читателя"))
                .andExpect(xpath("//*[@id='breadcrumb']/nav/ol/li[4]").string("Обновление данных читателя"))
                .andExpect(xpath("//*[@id='mainContent']/div/form/input[@name='_method'][@value='PATCH']").exists())
                .andExpect(xpath("//*[@id='mainContent']/div/form/div").nodeCount(2));
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
                .andExpect(redirectedUrl("/people/1"));
    }

    @Test
    void whenDeleteReader_thenRedirectToPeopleEndpoint() throws Exception {
        mockMvc.perform(delete("/people/{id}", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/people"));
    }
}
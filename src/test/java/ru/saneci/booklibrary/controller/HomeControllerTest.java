package ru.saneci.booklibrary.controller;

import org.junit.jupiter.api.Test;
import ru.saneci.booklibrary.BaseControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

class HomeControllerTest extends BaseControllerTest {

    @Test
    void whenGetAllReaders_thenIndexViewShouldContainRightLayout() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(xpath("/html/head/title").string("Реестр книг библиотеки - главная страница"))
                .andExpect(xpath("/html/body/h3").string("Основные разделы приложения"))
                .andExpect(xpath("/html/body/ul/li[1]/a").string("Добавление нового читателя"))
                .andExpect(xpath("/html/body/ul/li[2]/a").string("Список читателей"))
                .andExpect(xpath("/html/body/ul/li[3]/a").string("Добавление новой книги"))
                .andExpect(xpath("/html/body/ul/li[4]/a").string("Список книг"));
    }
}
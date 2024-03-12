package ru.saneci.booklibrary.controller;

import org.junit.jupiter.api.Test;
import ru.saneci.booklibrary.BaseControllerTest;

import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

class HomeControllerTest extends BaseControllerTest {

    @Test
    void whenGetIndex_thenIndexViewShouldContainTitle() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home/index"))
                .andExpect(xpath("/html/head/title").string("Главная страница - SB Library"));
    }

    @Test
    void whenGetIndex_thenIndexViewShouldContainNavbar() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(xpath("/html/body/header/nav/div/a").string("SB Library"))
                .andExpect(xpath("/html/body/header/nav/div/div/ul/li[1]/a").string("На главную"))
                .andExpect(xpath("/html/body/header/nav/div/div/ul/li[2]/a").string(startsWith("Книги")))
                .andExpect(xpath("/html/body/header/nav/div/div/ul/li[2]/ul/li[1]/a").string("Список книг"))
                .andExpect(xpath("/html/body/header/nav/div/div/ul/li[2]/ul/li[2]/a").string("Добавить новую книгу"))
                .andExpect(xpath("/html/body/header/nav/div/div/ul/li[3]/a").string(startsWith("Читатели")))
                .andExpect(xpath("/html/body/header/nav/div/div/ul/li[3]/ul/li[1]/a").string("Список читателей"))
                .andExpect(xpath("/html/body/header/nav/div/div/ul/li[3]/ul/li[2]/a").string("Добавить нового читателя"));
    }

    @Test
    void whenGetIndex_thenIndexViewShouldContainMainMenu() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(xpath("//*[@id='mainMenu']/nav/div[1]/h4").string("Основные разделы приложения"))
                .andExpect(xpath("//*[@id='mainMenu']/nav/div[2]/a/strong").string("Книги"))
                .andExpect(xpath("//*[@id='mainMenu']/nav/div[3]/a/strong").string("Читатели"))
                .andExpect(xpath("//*[@id='mainMenu']/nav/div[4]/a/strong").string("Новая книга"))
                .andExpect(xpath("//*[@id='mainMenu']/nav/div[5]/a/strong").string("Новый читатель"));
    }

    @Test
    void whenGetIndex_thenIndexViewShouldContainFooter() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(xpath("/html/body/footer/div/ul/li").nodeCount(4))
                .andExpect(xpath("/html/body/footer/div/div/p").exists())
                .andExpect(xpath("/html/body/footer/div/div/ul/li").nodeCount(3));
    }
}
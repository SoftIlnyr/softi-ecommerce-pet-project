package com.softi.productservice.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softi.productservice.dto.CategoryDto;
import com.softi.productservice.service.CategoryService;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@TestPropertySource(properties = {"mongock.enabled=false"})
@AutoConfigureMockMvc(addFilters = false)
class CategoryRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryService categoryService;

    @Test
    @SneakyThrows
    void findAll() {
        List<CategoryDto> all = categoryService.findAll();
        mvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(all)));
    }

    @Test
    @SneakyThrows
    void findCategoryById() {
        List<CategoryDto> categories = categoryService.findAll();
        CategoryDto categoryDto = categories.get(RandomUtils.secure().randomInt(0, categories.size()));
        mvc.perform(get("/api/categories/" + categoryDto.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(categoryDto)));
    }

    @Test
    @SneakyThrows
    void createCategory() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("test");

        RequestBuilder request = post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto));
        MvcResult mvcResult = mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        assertEquals(categoryDto.getName(), result.getName());
    }

    @Test
    @SneakyThrows
    void updateCategory() {
        CategoryDto oldCategoryDto = new CategoryDto();
        oldCategoryDto.setName("test");
        oldCategoryDto = categoryService.save(oldCategoryDto);

        CategoryDto newCategoryDto = new CategoryDto();
        newCategoryDto.setId(oldCategoryDto.getId());
        newCategoryDto.setName("test2");

        RequestBuilder request = post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCategoryDto));
        MvcResult mvcResult = mvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), CategoryDto.class);
        assertEquals(newCategoryDto.getName(), result.getName());

    }

    @Test
    @SneakyThrows
    void deleteCategoryById() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("test");
        categoryDto = categoryService.save(categoryDto);

        RequestBuilder request = delete("/api/categories/" + categoryDto.getId());
        mvc.perform(request)
                .andExpect(status().isOk());

        assertNull(categoryService.findById(categoryDto.getId()));
    }
}
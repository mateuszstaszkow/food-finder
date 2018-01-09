package com.foodfinder.integration.day;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.repository.DayRepository;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.utils.DayBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class DayIntegrationTest extends IntegrationTestSetup {

    @Autowired
    private DayRepository dayRepository;

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetDays_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/days"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndDay_whenPostDay_thenReturnsStatusCreated() throws Exception {
        addDayToDatabase(1L, 1L);
        addDayToDatabase(null, null);
        addDayToDatabase(3L, null);
        addDayToDatabase(null, 1L);
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndDay_whenGetDay_thenReturnsDay() throws Exception {
        DayDTO day = addDayToDatabase(5L, 1L);
        String fatName = day.getTimedDishes()
                .get(0)
                .getDish()
                .getProducts()
                .get(0)
                .getFat()
                .getName();

        mockMvc.perform(get("/api/days/" + day.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(day.getId().intValue())))
                .andExpect(jsonPath("$.timedDishes[0].dish.name",
                        is(day.getTimedDishes().get(0).getDish().getName())))
                .andExpect(jsonPath("$.timedDishes[0].dish.products[0].fat.name",
                        is(fatName)));
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndDay_whenUpdateDay_thenNotAddANewOne() throws Exception {
        addDayToDatabase(10L, 1L);

        long before = dayRepository.count();

        String result = mockMvc.perform(get("/api/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DayDTO day = (DayDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<DayDTO>>(){}))).get(0);
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mockMvc.perform(post("/api/days/" + day.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isOk());

        assertEquals(before, dayRepository.count());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndDay_whenUpdateDay_thenUpdateNameField() throws Exception {
        addDayToDatabase(20L, 1L);

        String result = mockMvc.perform(get("/api/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DayDTO day = (DayDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<DayDTO>>(){}))).get(0);
        day.getTimedDishes().get(0).setDishOrder(999);
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mockMvc.perform(post("/api/days/" + day.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/days/" + day.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timedDishes[0].dishOrder", is(999)));
    }

    private DayDTO addDayToDatabase(Long dayId, Long dishId) throws Exception {
        String result = mockMvc.perform(get("/api/products?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ProductDTO> products = mapper.readValue(result, new TypeReference<List<ProductDTO>>(){});
        DayDTO day = DayBuilder.getDayDTO(products, dayId, dishId);
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mockMvc.perform(post("/api/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isCreated());

        return day;
    }
}

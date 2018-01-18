package com.foodfinder.integration.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.repository.DayRepository;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.utils.DayBuilder;
import org.hamcrest.core.Is;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class AccountIntegrationTest extends IntegrationTestSetup {

    @Autowired
    private DayRepository dayRepository;

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetAccounts_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenAccount_whenUpdateAccount_thenReturnsStatusOk() throws Exception {
        BasicUserDTO account = BasicUserDTO.builder()
                .name("User")
                .surname("Userski")
                .email("user@foodfinder.com")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .build();
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mockMvc.perform(post("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenAccount_whenUpdateAccount_thenReturnsUpdatedDTO() throws Exception {
        BasicUserDTO account = BasicUserDTO.builder()
                .name("UserChanged")
                .surname("Userski")
                .email("user@foodfinder.com")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .build();
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mockMvc.perform(post("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(account.getName())));
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndDay_whenUpdateDay_thenNotAddANewOne() throws Exception {
        addDayToDatabase();

        long before = dayRepository.count();

        String result = mockMvc.perform(get("/api/users/me/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DayDTO day = (DayDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<DayDTO>>(){}))).get(0);
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mockMvc.perform(post("/api/users/me/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isOk());

        assertEquals(before, dayRepository.count());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenProductsAndDay_whenUpdateDay_thenUpdateDishOrderField() throws Exception {
        addDayToDatabase();

        String result = mockMvc.perform(get("/api/users/me/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DayDTO day = (DayDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<DayDTO>>(){}))).get(0);
        day.getTimedDishes().get(0).setDishOrder(999);
        String jsonDay = new ObjectMapper().writeValueAsString(day);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(day.getDate());

        mockMvc.perform(post("/api/users/me/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/me/days/" + formattedDate)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timedDishes[0].dishOrder", Is.is(999)));
    }

    private void addDayToDatabase() throws Exception {
        String result = mockMvc.perform(get("/api/products?page=0&size=10")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<ProductDTO> products = mapper.readValue(result, new TypeReference<List<ProductDTO>>(){});
        DayDTO day = DayBuilder.getDayDTO(products, null, null);
        day.setDate(new Date(10000));
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mockMvc.perform(post("/api/users/me/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isCreated());
    }
}
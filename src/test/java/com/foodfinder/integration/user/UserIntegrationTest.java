package com.foodfinder.integration.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.repository.DayRepository;
import com.foodfinder.food.domain.dto.ProductDTO;
import com.foodfinder.integration.config.IntegrationTestSetup;
import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.utils.DayBuilder;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserIntegrationTest extends IntegrationTestSetup {

    @Autowired
    private DayRepository dayRepository;

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void whenGetUsers_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenUser_whenPostUser_thenReturnsStatusCreated() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(1L)
                .name("User")
                .surname("Userski")
                .email("user1@foodfinder.com")
                .role(new RoleDTO(2L))
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(null)
                .build();
        String jsonUser = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenUserWithNullId_whenPostUser_thenReturnsStatusCreated() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(null)
                .name("User")
                .surname("Userski")
                .email("userx@foodfinder.com")
                .role(new RoleDTO(2L))
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(null)
                .build();
        String jsonUser = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenUser_whenGetUser_thenReturnsUser() throws Exception {
        UserDTO user = UserDTO.builder()
                .id(3L)
                .name("User")
                .surname("Userski")
                .email("user3@foodfinder.com")
                .role(new RoleDTO(2L))
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(null)
                .build();
        String jsonUser = new ObjectMapper().writeValueAsString(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenProductsAndDay_whenUpdateDay_thenNotAddANewOne() throws Exception {
        addDayToDatabase();

        long before = dayRepository.count();

        String result = mockMvc.perform(get("/api/users/1/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DayDTO day = (DayDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<DayDTO>>(){}))).get(0);
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mockMvc.perform(post("/api/users/1/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isOk());

        assertEquals(before, dayRepository.count());
    }

    @Test
    @WithMockUser(username = "admin@foodfinder.com", password = "zoliborz")
    public void givenProductsAndDay_whenUpdateDay_thenUpdateDishOrderField() throws Exception {
        addDayToDatabase();

        String result = mockMvc.perform(get("/api/users/1/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        DayDTO day = (DayDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<DayDTO>>(){}))).get(0);
        day.getTimedDishes().get(0).setDishOrder(999);
        String jsonDay = new ObjectMapper().writeValueAsString(day);
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(day.getDate());

        mockMvc.perform(post("/api/users/1/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/users/1/days/" + formattedDate)
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

        mockMvc.perform(post("/api/users/1/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isCreated());
    }
}
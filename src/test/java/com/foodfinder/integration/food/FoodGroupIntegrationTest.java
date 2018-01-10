package com.foodfinder.integration.food;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.repository.FoodGroupRepository;
import com.foodfinder.integration.config.IntegrationTestSetup;
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
public class FoodGroupIntegrationTest extends IntegrationTestSetup {

    @Autowired
    private FoodGroupRepository foodGroupRepository;
    
    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void whenGetFoodGroups_thenReturnsStatusOk() throws Exception {
        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenFoodGroup_whenPostFoodGroup_thenReturnsStatusCreated() throws Exception {
        FoodGroupDTO foodGroup = new FoodGroupDTO(1L, "Test foodGroups 1");
        String jsonFoodGroup = new ObjectMapper().writeValueAsString(foodGroup);

        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFoodGroup))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenFoodGroupWithNullId_whenPostFoodGroup_thenReturnsStatusCreated() throws Exception {
        FoodGroupDTO foodGroup = new FoodGroupDTO(null, "Test foodGroups 2");
        String jsonFoodGroup = new ObjectMapper().writeValueAsString(foodGroup);

        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFoodGroup))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenFoodGroup_whenGetFoodGroup_thenReturnsFoodGroup() throws Exception {
        FoodGroupDTO foodGroup = new FoodGroupDTO(1L, "Test foodGroups 3");
        String jsonFoodGroup = new ObjectMapper().writeValueAsString(foodGroup);

        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFoodGroup))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/groups/" + foodGroup.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(foodGroup.getName())));
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenFoodGroup_whenGetFoodGroupByName_thenReturnsFoodGroupArray() throws Exception {
        FoodGroupDTO foodGroup = new FoodGroupDTO(null, "Test foodGroups 4");
        String jsonFoodGroup = new ObjectMapper().writeValueAsString(foodGroup);

        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFoodGroup))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/groups?name=Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(foodGroup.getName())));
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenFoodGroupsAndFoodGroup_whenUpdateFoodGroup_thenNotAddANewOne() throws Exception {
        addFoodGroupToDatabase("Test foodGroups 5");

        long before = foodGroupRepository.count();

        String result = mockMvc.perform(get("/api/groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        FoodGroupDTO foodGroup = (FoodGroupDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<FoodGroupDTO>>(){}))).get(0);
        String jsonFoodGroup = new ObjectMapper().writeValueAsString(foodGroup);

        mockMvc.perform(post("/api/groups/" + foodGroup.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFoodGroup))
                .andExpect(status().isOk());

        assertEquals(before, foodGroupRepository.count());
    }

    @Test
    @WithMockUser(username = "user@foodfinder.com", password = "mokotow")
    public void givenFoodGroupsAndFoodGroup_whenUpdateFoodGroup_thenUpdateNameField() throws Exception {
        addFoodGroupToDatabase("Test foodGroups 6");

        String result = mockMvc.perform(get("/api/groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        FoodGroupDTO foodGroup = (FoodGroupDTO) ((ArrayList)(mapper.readValue(result, new TypeReference<List<FoodGroupDTO>>(){}))).get(0);
        foodGroup.setName("Updated foodGroup");
        String jsonFoodGroup = new ObjectMapper().writeValueAsString(foodGroup);

        mockMvc.perform(post("/api/groups/" + foodGroup.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFoodGroup))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/groups/" + foodGroup.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(foodGroup.getName())));
    }

    private void addFoodGroupToDatabase(String name) throws Exception {
        FoodGroupDTO foodGroup = new FoodGroupDTO(null, name);
        String jsonFoodGroup = new ObjectMapper().writeValueAsString(foodGroup);

        mockMvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFoodGroup))
                .andExpect(status().isCreated());
    }
}
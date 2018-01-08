package com.foodfinder.food.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.food.domain.dto.FoodGroupDTO;
import com.foodfinder.food.service.FoodGroupService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FoodGroupRestController.class)
@EnableSpringDataWebSupport
public class FoodGroupRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FoodGroupService foodGroupService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenFoodGroup_whenGetFoodGroup_thenReturnJson() throws Exception {

        FoodGroupDTO foodGroup = new FoodGroupDTO(1L, "Test products");

        given(foodGroupService.getFoodGroup(1L)).willReturn(foodGroup);

        mvc.perform(get("/api/groups/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(foodGroup.getName())));
    }

    @Test
    @WithMockUser
    public void givenFoodGroups_whenGetFoodGroups_thenReturnJsonArray() throws Exception {

        FoodGroupDTO foodGroup = new FoodGroupDTO(1L, "Test products");
        List<FoodGroupDTO> allFoodGroups = Collections.singletonList(foodGroup);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(foodGroupService.getFoodGroupList(defaultPageRequest, null)).willReturn(allFoodGroups);

        mvc.perform(get("/api/groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(foodGroup.getName())));
    }

    @Test
    @WithMockUser
    public void givenFoodGroupsName_whenGetFoodGroups_thenReturnJsonArray() throws Exception {

        FoodGroupDTO foodGroup = new FoodGroupDTO(1L, "Test products");
        List<FoodGroupDTO> allFoodGroups = Collections.singletonList(foodGroup);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(foodGroupService.getFoodGroupList(defaultPageRequest, "pierogi")).willReturn(allFoodGroups);

        mvc.perform(get("/api/groups?name=pierogi")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(foodGroup.getName())));
    }

    @Test
    @WithMockUser
    public void givenFoodGroup_whenAddFoodGroup_thenReturnStatusCreated() throws Exception {

        FoodGroupDTO foodGroup = new FoodGroupDTO(1L, "Test products");
        String jsonFoodGroup = new ObjectMapper().writeValueAsString(foodGroup);

        mvc.perform(post("/api/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonFoodGroup))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenUnauthorizedUser_thenReturnStatusUnauthorized() throws Exception {

        mvc.perform(get("/api/groups"))
                .andExpect(status().isUnauthorized());
    }
}
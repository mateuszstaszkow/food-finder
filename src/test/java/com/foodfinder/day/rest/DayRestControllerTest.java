package com.foodfinder.day.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.service.DayService;
import com.foodfinder.user.service.AccountService;
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
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(DayRestController.class)
@EnableSpringDataWebSupport
public class DayRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DayService dayService;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser("VIEW_DAYS")
    public void givenDay_whenGetDay_thenReturnJson() throws Exception {

        DayDTO day = DayDTO.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(0))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();

        given(dayService.getDay(1L)).willReturn(day);

        mvc.perform(get("/api/days/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(day.getName())));
    }

    @Test
    @WithMockUser("VIEW_DAYS")
    public void givenDays_whenGetDaysNoPersonal_thenReturnJsonArray() throws Exception {

        DayDTO day = DayDTO.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(0))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();
        List<DayDTO> allDays = Collections.singletonList(day);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(dayService.getDayList(defaultPageRequest,null,null,null))
                .willReturn(allDays);

        mvc.perform(get("/api/days?personal=false")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(day.getName())));
    }

    @Test
    @WithMockUser("VIEW_DAYS")
    public void givenDays_whenGetDays_thenReturnJsonArray() throws Exception {

        DayDTO day = DayDTO.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(0))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();
        List<DayDTO> allDays = Collections.singletonList(day);

        given(accountService.getAccountDays(null,null,null))
                .willReturn(allDays);

        mvc.perform(get("/api/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(day.getName())));
    }

    @Test
    @WithMockUser("VIEW_DAYS")
    public void givenDay_whenAddDay_thenReturnStatusCreated() throws Exception {

        DayDTO day = DayDTO.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(0))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();
        String jsonDay = new ObjectMapper().writeValueAsString(day);

        mvc.perform(post("/api/days")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonDay))
                .andExpect(status().isCreated());
    }
}
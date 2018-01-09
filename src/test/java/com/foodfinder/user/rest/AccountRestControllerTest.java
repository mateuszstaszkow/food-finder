package com.foodfinder.user.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.user.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
@WebMvcTest(AccountRestController.class)
@EnableSpringDataWebSupport
public class AccountRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenAccount_whenGetAccount_thenReturnJson() throws Exception {
        BasicUserDTO account = BasicUserDTO.builder()
                .name("User")
                .surname("Userski")
                .email("account@account.com")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();

        given(accountService.getAccount()).willReturn(account);

        mvc.perform(get("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(account.getName())));
    }

    @Test
    @WithMockUser
    public void givenAccount_whenUpdateAccount_thenReturnStatusOk() throws Exception {

        BasicUserDTO account = BasicUserDTO.builder()
                .name("User")
                .surname("Userski")
                .email("account@account.com")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();
        String jsonAccount = new ObjectMapper().writeValueAsString(account);

        mvc.perform(post("/api/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonAccount))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void givenDays_whenGetAccountDays_thenReturnJsonArray() throws Exception {

        DayDTO dayDTO = DayDTO.builder()
                .id(1L)
                .date(new Date(10))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();
        List<DayDTO> daysDTO = Collections.singletonList(dayDTO);

        given(accountService.getAccountDays(null, null)).willReturn(daysDTO);

        mvc.perform(get("/api/users/me/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dayDTO.getId().intValue())));
    }

    @Test
    @WithMockUser
    public void givenDay_whenGetAccountDay_thenReturnJson() throws Exception {
        DayDTO dayDTO = DayDTO.builder()
                .id(1L)
                .date(new Date(0))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();

        given(accountService.getAccountDays(new Date(-3600000))).willReturn(dayDTO);

        mvc.perform(get("/api/users/me/days/1970-01-01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dayDTO.getId().intValue())));
    }

    @Test
    public void givenUnauthorizedAccount_thenReturnStatusUnauthorized() throws Exception {

        mvc.perform(get("/api/users/me"))
                .andExpect(status().isUnauthorized());
    }
}

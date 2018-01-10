package com.foodfinder.user.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.rest.ExceptionRestController;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.service.UserService;
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
@WebMvcTest(UserRestController.class)
@EnableSpringDataWebSupport
public class UserRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private ExceptionRestController exceptionRestController;

    @Test
    @WithMockUser
    public void givenUser_whenGetUser_thenReturnJson() throws Exception {

        UserDTO user = UserDTO.builder()
                .name("User")
                .surname("Userski")
                .email("user@user.com")
                .role(new RoleDTO())
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();

        given(userService.getUser(1L)).willReturn(user);

        mvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(user.getName())));
    }

    @Test
    @WithMockUser
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {

        UserDTO user = UserDTO.builder()
                .name("User")
                .surname("Userski")
                .email("user@user.com")
                .role(new RoleDTO())
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();
        List<UserDTO> allUsers = Collections.singletonList(user);
        PageRequest defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        given(userService.getUserList(defaultPageRequest)).willReturn(allUsers);

        mvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(user.getName())));
    }

    @Test
    @WithMockUser
    public void givenUser_whenAddUser_thenReturnStatusCreated() throws Exception {

        UserDTO user = UserDTO.builder()
                .name("User")
                .surname("Userski")
                .email("user@user.com")
                .role(new RoleDTO())
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();
        String jsonUser = new ObjectMapper().writeValueAsString(user);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUser))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    public void givenDays_whenGetUserDays_thenReturnJsonArray() throws Exception {

        DayDTO dayDTO = DayDTO.builder()
                .id(1L)
                .date(new Date(10))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();
        List<DayDTO> daysDTO = Collections.singletonList(dayDTO);

        given(userService.getUserDays(1L, null, null)).willReturn(daysDTO);

        mvc.perform(get("/api/users/1/days")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(dayDTO.getId().intValue())));
    }

    @Test
    @WithMockUser
    public void givenDay_whenGetUserDay_thenReturnJson() throws Exception {

        Date date = new Date(0);

        DayDTO dayDTO = DayDTO.builder()
                .id(1L)
                .date(date)
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();

        given(userService.getUserDay(1L, new Date(-3600000))).willReturn(dayDTO);

        mvc.perform(get("/api/users/1/days/1970-01-01")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(dayDTO.getId().intValue())));
    }

    @Test
    public void givenUnauthorizedUser_thenReturnStatusUnauthorized() throws Exception {

        mvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }
}

package com.foodfinder.user;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.day.domain.entity.TimedDish;
import com.foodfinder.day.domain.mapper.DayMapper;
import com.foodfinder.user.domain.dto.RoleDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.domain.entity.Role;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.repository.UserRepository;
import com.foodfinder.user.service.UserService;
import com.foodfinder.utils.RestControllerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private DayMapper dayMapper;

    private UserService userService;
    private PageRequest defaultPageRequest;
    private User user;
    private UserDTO userDTO;
    private List<UserDTO> usersDTO;
    private Page<User> usersPage;
    private Set<Day> days;
    private List<DayDTO> daysDTO;

    @Before
    public void setup() {
        userService = new UserService(userRepository, userMapper, dayMapper);
        defaultPageRequest = RestControllerTestUtils.getDefaultPageRequest();

        user = User.builder()
                .id(1L)
                .name("User")
                .surname("Userski")
                .email("user@user.com")
                .password("encryptedpassword")
                .enabled(true)
                .role(new Role())
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(new HashSet<>(Collections.singletonList(new Day())))
                .build();
        usersPage = new PageImpl<>(Collections.singletonList(user));

        userDTO = UserDTO.builder()
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
        usersDTO = Collections.singletonList(userDTO);

        Day day = Day.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(10))
                .timedDishes(new HashSet<>(Collections.singletonList(new TimedDish())))
                .build();
        days = new HashSet<>(Collections.singletonList(day));

        DayDTO dayDTO = DayDTO.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(10))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();
        daysDTO = Collections.singletonList(dayDTO);
    }

    @Test
    public void givenUserId_whenGetUser_thenReturnDto() throws Exception {
        given(userRepository.findOne(1L)).willReturn(user);
        given(userMapper.toDto(user)).willReturn(userDTO);

        assertEquals(userService.getUser(1L), userDTO);
    }

    @Test
    public void whenGetUsers_thenReturnDtoList() throws Exception {
        given(userRepository.findAll(defaultPageRequest)).willReturn(usersPage);
        given(userMapper.userListToDto(usersPage)).willReturn(usersDTO);

        assertEquals(userService.getUserList(defaultPageRequest), usersDTO);
    }

    @Test
    public void whenGetDayList_thenReturnDtoList() throws Exception {
        given(userRepository.findUserDays(1L)).willReturn(days);
        given(dayMapper.dayListToDto(days)).willReturn(daysDTO);

        assertEquals(userService.getUserDays(1L, null, null, null), daysDTO);
    }

    @Test
    public void givenDate_whenGetDayList_thenReturnDtoList() throws Exception {
        given(userRepository.findUserDaysByDateBetween(1L, new Date(-3600000), new Date(82799000))).willReturn(days);
        given(dayMapper.dayListToDto(days)).willReturn(daysDTO);

        assertEquals(userService.getUserDays(1L, new Date(1000), null, null), daysDTO);
    }

    @Test
    public void givenDateFromAndTo_whenGetDayList_thenReturnDtoList() throws Exception {
        Date from = new Date(0);
        Date to = new Date(1000);

        given(userRepository.findUserDaysByDateBetween(1L, from, to)).willReturn(days);
        given(dayMapper.dayListToDto(days)).willReturn(daysDTO);

        assertEquals(userService.getUserDays(1L, null, from, to), daysDTO);
    }


}
package com.foodfinder.user;

import com.foodfinder.container.configuration.security.LoggedUserGetter;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.day.domain.dto.TimedDishDTO;
import com.foodfinder.day.domain.entity.Day;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.user.domain.entity.Role;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import com.foodfinder.user.service.AccountService;
import com.foodfinder.user.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class AccountServiceTest {

    @MockBean
    private UserService userService;

    @MockBean
    private LoggedUserGetter loggedUserGetter;

    @MockBean
    private UserMapper userMapper;

    private AccountService accountService;
    private User account;

    @Before
    public void setup() {
        accountService = new AccountService(userService, loggedUserGetter, userMapper);

        account = User.builder()
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
    }

    @Test
    public void givenAccountId_whenGetAccount_thenReturnDto() throws Exception {
        BasicUserDTO accountDTO = BasicUserDTO.builder()
                .name("User")
                .surname("Userski")
                .email("account@account.com")
                .weight(100f)
                .height(200f)
                .age(20)
                .gender("male")
                .days(Collections.singletonList(new DayDTO()))
                .build();

        given(loggedUserGetter.getLoggedUser()).willReturn(account);
        given(userMapper.toBasicDto(account)).willReturn(accountDTO);

        assertEquals(accountService.getAccount(), accountDTO);
    }

    @Test
    public void whenGetDayList_thenReturnDtoList() throws Exception {
        DayDTO dayDTO = DayDTO.builder()
                .id(1L)
                .name("Monday")
                .date(new Date(10))
                .timedDishes(Collections.singletonList(new TimedDishDTO()))
                .build();
        List<DayDTO> daysDTO = Collections.singletonList(dayDTO);

        given(loggedUserGetter.getLoggedUser()).willReturn(account);
        given(userService.getUserDays(1L, null, null, null)).willReturn(daysDTO);

        assertEquals(accountService.getAccountDays( null, null, null), daysDTO);
    }
}
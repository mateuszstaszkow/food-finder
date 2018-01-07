package com.foodfinder.user.service;

import com.foodfinder.container.configuration.security.LoggedUserGetter;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

    private final UserService userService;
    private final LoggedUserGetter loggedUserGetter;
    private final UserMapper userMapper;

    public BasicUserDTO getAccount() {
        User loggedUser = loggedUserGetter.getLoggedUser();
        return userMapper.toBasicDto(loggedUser);
    }

    public void updateAccount(BasicUserDTO user) {
        Long loggedUserId = loggedUserGetter.getLoggedUser().getId();
        userService.updateBasicUser(loggedUserId, user);
    }

    public List<DayDTO> getAccountDays(Date date, Date from, Date to) {
        Long loggedUserId = loggedUserGetter.getLoggedUser().getId();
        return userService.getUserDays(loggedUserId, date, from, to);
    }

    public void addDayToAccount(DayDTO dayDTO) {
        if(dayDTO == null) {
            return;
        }
        BasicUserDTO loggedUser = getAccount();
        List<DayDTO> days = loggedUser.getDays() == null ? new ArrayList<>() : loggedUser.getDays();
        days.add(dayDTO);
        loggedUser.setDays(days);
        updateAccount(loggedUser);
    }
}

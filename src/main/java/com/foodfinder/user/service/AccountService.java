package com.foodfinder.user.service;

import com.foodfinder.container.configuration.security.LoggedUserGetter;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountService {

    private final UserService userService;
    private final LoggedUserGetter loggedUserGetter;

    public BasicUserDTO getAccount() {
        Long loggedUserId = loggedUserGetter.getLoggedUser().getId();
        return userService.getBasicUser(loggedUserId);
    }

    public void updateAccount(BasicUserDTO user) {
        Long loggedUserId = loggedUserGetter.getLoggedUser().getId();
        userService.updateBasicUser(loggedUserId, user);
    }

    public List<DayDTO> getAccountDays(Date date, Date from, Date to) {
        Long loggedUserId = loggedUserGetter.getLoggedUser().getId();
        return userService.getUserDays(loggedUserId, date, from, to);
    }
}

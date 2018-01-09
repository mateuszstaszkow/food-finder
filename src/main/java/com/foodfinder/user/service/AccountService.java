package com.foodfinder.user.service;

import com.foodfinder.container.configuration.security.LoggedUserGetter;
import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.domain.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public List<DayDTO> getAccountDays(Date from, Date to) {
        Long loggedUserId = loggedUserGetter.getLoggedUser().getId();
        return userService.getUserDays(loggedUserId, from, to);
    }

    public DayDTO getAccountDays(Date date) {
        Long loggedUserId = loggedUserGetter.getLoggedUser().getId();
        return userService.getUserDay(loggedUserId, date);
    }

    public ResponseEntity<?> addOrUpdateAccountDay(DayDTO dayDTO) {
        Long loggedUserId = loggedUserGetter.getLoggedUser().getId();
        return userService.addOrUpdateUserDay(loggedUserId, dayDTO);
    }
}

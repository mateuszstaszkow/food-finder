package com.foodfinder.user.rest;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.user.domain.dto.BasicUserDTO;
import com.foodfinder.user.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/users/me")
class AccountRestController {

    private final AccountService accountService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public BasicUserDTO getAccount() {
        return accountService.getAccount();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@RequestBody BasicUserDTO userDTO) {
        accountService.updateAccount(userDTO);
    }

    @RequestMapping(value = "/days", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DayDTO> getUserDays(@RequestParam(value="date", required = false)
                                    @DateTimeFormat(pattern="yyyy-MM-dd") Date date,
                                    @RequestParam(value="from", required = false)
                                    @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                    @RequestParam(value="to", required = false)
                                    @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
        return accountService.getAccountDays(date, from, to);
    }
}
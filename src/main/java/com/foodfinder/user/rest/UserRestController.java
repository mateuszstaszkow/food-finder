package com.foodfinder.user.rest;

import com.foodfinder.day.domain.dto.DayDTO;
import com.foodfinder.user.domain.dto.UserDTO;
import com.foodfinder.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}/users")
class UserRestController {

    private final UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> getUserList(Pageable pageable) {
        return userService.getUserList(pageable);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void addUser(@RequestBody UserDTO userDTO) {
        userService.postUser(userDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userService.updateUser(id, userDTO);
    }

    @RequestMapping(value = "/{id}/days", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<DayDTO> getUserDays(@PathVariable Long id,
                                    @RequestParam(value="from", required = false)
                                        @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                    @RequestParam(value="to", required = false)
                                        @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
        return userService.getUserDays(id, from, to);
    }

    @RequestMapping(value = "/{id}/days", method = RequestMethod.POST)
    public ResponseEntity<?> postOrUpdateUserDay(@PathVariable Long id, @RequestBody DayDTO dayDTO) {
        return userService.addOrUpdateUserDay(id, dayDTO);
    }

    @RequestMapping(value = "/{id}/days/{date}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public DayDTO getUserDays(@PathVariable Long id,
                                    @PathVariable("date") @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
        return userService.getUserDay(id, date);
    }
}
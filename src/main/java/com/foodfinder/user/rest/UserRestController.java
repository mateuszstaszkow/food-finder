package com.foodfinder.user.rest;

import com.foodfinder.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/${food-finder.prefix}")
class UserRestController {

    private final UserService userService;

    @RequestMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public String getFoodList() {
        return userService.getFoodList();
    }
}
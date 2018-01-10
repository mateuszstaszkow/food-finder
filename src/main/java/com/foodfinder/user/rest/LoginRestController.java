package com.foodfinder.user.rest;

import com.foodfinder.user.domain.dto.RegistrationDTO;
import com.foodfinder.user.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class LoginRestController {

    private final LoginService loginService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody RegistrationDTO user) {
        loginService.registerUser(user);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void loginUser() {}
}
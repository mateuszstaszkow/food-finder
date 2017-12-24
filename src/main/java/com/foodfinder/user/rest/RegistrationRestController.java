package com.foodfinder.user.rest;

import com.foodfinder.user.domain.dto.RegistrationDTO;
import com.foodfinder.user.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/register")
class RegistrationRestController {

    private final RegistrationService registrationService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@RequestBody RegistrationDTO user) {
        registrationService.registerUser(user);
    }
}
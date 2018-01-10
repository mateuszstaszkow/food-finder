package com.foodfinder.container.configuration.security;

import com.foodfinder.user.repository.UserRepository;
import com.foodfinder.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoggedUserGetter {

    private final UserRepository userRepository;
    private final BackendAuthenticator backendAuthenticator;

    public User getLoggedUser() {
        return userRepository.findByEmail(getLoggedUserEmail());
    }

    public UserDetails getLoggedUserDetails() {
        UserDetails loggedUserDetails = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (backendAuthenticator.isAuthenticated(authentication)) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                loggedUserDetails = ((UserDetails) principal);
            } else {
                throw new RuntimeException("Expected class of authentication principal is AuthenticationUserDetails. Given: " + principal.getClass());
            }
        }
        return loggedUserDetails;
    }

    public String getLoggedUserEmail() {
        return getLoggedUserDetails().getUsername();
    }

}
package com.foodfinder.container.configuration.security;

import com.foodfinder.user.domain.entity.User;
import com.foodfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import java.util.Optional;

@Service("loggedUserGetter")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoggedUserGetter {

    private final UserRepository userRepository;
    private final BackendAuthenticator backendAuthenticator;

    public User getLoggedUser() {
        return Optional.ofNullable(userRepository.findByEmail(getLoggedUserEmail()))
                .orElseThrow(NotFoundException::new);
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
        UserDetails userDetails = getLoggedUserDetails();
        if(userDetails == null || userDetails.getUsername() == null) {
            throw new NotAuthorizedException("User unauthorized");
        }
        return userDetails.getUsername();
    }

}
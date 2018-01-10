package com.foodfinder.container.configuration.security;

import com.foodfinder.user.repository.UserRepository;
import com.foodfinder.user.domain.entity.Privilege;
import com.foodfinder.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class FoodFinderUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                return null;
            }
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(), getAuthorities(user.getRole().getPrivileges()));
        }
        catch (Exception e){
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Set<GrantedAuthority> getAuthorities(Set<Privilege> privileges){
        Set<GrantedAuthority> authorities = new HashSet<>();
        privileges.stream()
                .map(Privilege::getName)
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        return authorities;
    }


}
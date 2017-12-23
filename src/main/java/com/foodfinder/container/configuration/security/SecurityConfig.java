package com.foodfinder.container.configuration.security;

import com.foodfinder.user.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean())
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return new FoodFinderUserDetailsService(userRepository);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeRequests()
                    .antMatchers("/api/days/**").hasAuthority("VIEW_DAYS")
                    .antMatchers("/api/diagntostics/**").hasAuthority("VIEW_ADMIN")
                    .antMatchers("/api/diets/**").hasAuthority("VIEW_DIETS")
                    .antMatchers("/api/dishes/**").hasAuthority("VIEW_DISHES")
                    .antMatchers("/api/groups/**").hasAuthority("VIEW_GROUPS")
                    .antMatchers("/api/products/**").hasAuthority("VIEW_PRODUCTS")
                    .antMatchers("/api/recognize/**").hasAuthority("RECOGNIZE")
                    .antMatchers("/api/migrate/**").hasAuthority("MIGRATE")
                    .antMatchers("/api/translate/**").hasAuthority("TRANSLATE")
                    .antMatchers("/api/users/**").hasAuthority("VIEW_USERS")
                .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login");
    }

}
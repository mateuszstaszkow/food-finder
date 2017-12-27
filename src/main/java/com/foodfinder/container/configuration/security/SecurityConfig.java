package com.foodfinder.container.configuration.security;

import com.foodfinder.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Value("${food-finder.prefix}")
    private String foodFinderPrefix;

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

    @Bean
    public UsernamePasswordAuthenticationFilter authFilter() throws Exception {
        UsernamePasswordAuthenticationFilter authFilter = new UsernamePasswordAuthenticationFilter();
        authFilter.setAuthenticationManager(this.authenticationManagerBean());
        authFilter.setAuthenticationSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()));

        return authFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .disable()
                .authorizeRequests()
                    .antMatchers("/" + foodFinderPrefix + "/days/**").hasAuthority("VIEW_DAYS")
                    .antMatchers("/" + foodFinderPrefix + "/diagntostics/**").hasAuthority("VIEW_ADMIN")
                    .antMatchers("/" + foodFinderPrefix + "/diets/**").hasAuthority("VIEW_DIETS")
                    .antMatchers("/" + foodFinderPrefix + "/dishes/**").hasAuthority("VIEW_DISHES")
                    .antMatchers("/" + foodFinderPrefix + "/groups/**").hasAuthority("VIEW_GROUPS")
                    .antMatchers("/" + foodFinderPrefix + "/products/**").hasAuthority("VIEW_PRODUCTS")
                    .antMatchers("/" + foodFinderPrefix + "/recognize/**").hasAuthority("RECOGNIZE")
                    .antMatchers("/" + foodFinderPrefix + "/migrate/**").hasAuthority("MIGRATE")
                    .antMatchers("/" + foodFinderPrefix + "/translate/**").hasAuthority("TRANSLATE")
                    .antMatchers("/" + foodFinderPrefix + "/users/**").hasAuthority("VIEW_USERS")
                    .antMatchers("/" + foodFinderPrefix + "/roles/**").hasAuthority("VIEW_ROLES")
                    .antMatchers("/" + foodFinderPrefix + "/privileges/**").hasAuthority("VIEW_PRIVILEGES")
                    .antMatchers("/" + foodFinderPrefix + "/**").authenticated()
                    .antMatchers("/v2/**").hasAuthority("VIEW_ADMIN")
                    .antMatchers("/swagger-ui.html/**").hasAuthority("VIEW_ADMIN")
                    .and()
                .addFilter(authFilter())
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpStatus.OK.value()));
    }

}
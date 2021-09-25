package com.ironhack.midtermProject.security;

import com.ironhack.midtermProject.service.impl.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.csrf().disable();
        http.authorizeRequests()
                .mvcMatchers(HttpMethod.GET,"/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH,"/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/create/checking").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/create/savings").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/create/credit_card").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.GET,"/accounts").hasAnyRole("ACCOUNT_HOLDER")
                .anyRequest().permitAll();
    }
}

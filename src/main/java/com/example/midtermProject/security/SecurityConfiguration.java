package com.example.midtermProject.security;

import com.example.midtermProject.service.impl.CustomUserDetailsService;
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



    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder.encode("123456"))
                .roles("ADMIN", "USER")
                .and()
                .withUser("user")
                .password(passwordEncoder
                        .encode("123456"))
                .roles("USER");
    }*/

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
                .mvcMatchers(HttpMethod.GET,"/checking/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH,"/checking/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.GET,"/student-checking/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH,"/student-checking/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.GET,"/savings/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH,"/savings/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.GET,"/credit-card/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH,"/credit-card/balance/{id}").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/checking/create").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/savings/create").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.POST,"/credit-card/create").hasAnyRole("ADMIN")
                .mvcMatchers(HttpMethod.GET,"/checking/primary").hasAnyRole("ACCOUNT_HOLDER")
                .mvcMatchers(HttpMethod.GET,"/checking/secondary").hasAnyRole("ACCOUNT_HOLDER")
                .mvcMatchers(HttpMethod.GET,"/student-checking/secondary").hasAnyRole("ACCOUNT_HOLDER")
                .mvcMatchers(HttpMethod.GET,"/savings/primary").hasAnyRole("ACCOUNT_HOLDER")
                .mvcMatchers(HttpMethod.GET,"/savings/primary").hasAnyRole("ACCOUNT_HOLDER")
                .mvcMatchers(HttpMethod.GET,"/credit-card/primary").hasAnyRole("ACCOUNT_HOLDER")
                .mvcMatchers(HttpMethod.GET,"/credit-card/secondary").hasAnyRole("ACCOUNT_HOLDER")
                .anyRequest().permitAll();
    }
}

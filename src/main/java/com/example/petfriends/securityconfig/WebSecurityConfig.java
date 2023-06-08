package com.example.petfriends.securityconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/h2-console/login.do*").permitAll()
                .antMatchers("/resources/**", "/static/**", "/plugins/**", "/css/**", "/js/**", "/images/**","/fonts/**", "/jquery/**", "/bootstrap/**",
                        "/register", "/index", "/").permitAll()
                .antMatchers( "/admin/*", "/request/*").hasRole("ADMIN")
                .antMatchers("/event/add", "/event/edit/*", "/event/delete/*").hasRole("EVENT_PLANNER")
                .antMatchers("/post/*", "/user/*","/event/*", "/comment/{idPost}/user/{username}",
                        "/petfriendly/*", "/saveLocation", "/petshop/*", "/becomePlanner/{idUser}",
                        "/requestSent", "/follow/*", "/unfollow/*", "/followers/*", "/following/*",
                        "/like/*", "/removeLike/*").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .defaultSuccessUrl("/")
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/access-denied");

        http.logout().logoutSuccessUrl("/");
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

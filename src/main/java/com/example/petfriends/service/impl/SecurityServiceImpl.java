package com.example.petfriends.service.impl;

import com.example.petfriends.model.Admin;
import com.example.petfriends.model.Role;
import com.example.petfriends.model.User;
import com.example.petfriends.repository.AdminRepository;
import com.example.petfriends.repository.UserRepository;
import com.example.petfriends.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;


    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isPresent()){
            user = userOptional.get();
            log.info("User is {} with password {}", user.getUsername(), user.getPassword());
            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(),
                    getAuthorities(user.getRoles()));
        }

        Admin admin;
        Optional<Admin> adminOptional = adminRepository.findByUsername(username);
        if(adminOptional.isPresent()){
            admin = adminOptional.get();
            log.info("User is {} with password {}", admin.getUsername(), admin.getPassword());
            return new org.springframework.security.core.userdetails.User(admin.getUsername(),
                    admin.getPassword(),
                    getAuthorities(admin.getRoles()));
        }

        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> authorities) {
        if (authorities == null) {
            return new HashSet<>();
        } else if (authorities.size() == 0) {
            return new HashSet<>();
        } else {
            log.info("Role is {} ", authorities.stream()
                    .map(Role::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet()));
            return authorities.stream()
                    .map(Role::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());
        }
    }
}

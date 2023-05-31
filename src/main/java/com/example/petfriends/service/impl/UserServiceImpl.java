package com.example.petfriends.service.impl;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.*;
import com.example.petfriends.repository.ImageRepository;
import com.example.petfriends.repository.PetRepository;
import com.example.petfriends.repository.RoleRepository;
import com.example.petfriends.repository.UserRepository;
import com.example.petfriends.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;


    @Override
    public User save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Set<Role> userRoles = roleRepository.findAll()
                .stream()
                .filter(x -> x.getName().equals(RoleEnum.ROLE_USER))
                .collect(Collectors.toSet());
        if (userRoles.isEmpty()) {
            Role role = roleRepository.save(new Role(RoleEnum.ROLE_USER));
            userRoles.add(role);
        }
        user.setRoles(userRoles);

        return userRepository.save(user);
    }

    @Override
    public User saveWithoutHash(User user) {
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User with username: " +  username + "not found");
        }
        return userOptional.get();
    }
    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByUsername(authentication.getName());
    }

    @Override
    public List<User> findByUsernameSearch(String searchInput) {
        return userRepository.findByUsernameSearch(searchInput);
    }

    @Override
    public User findById(Long idUser) {
        Optional<User> userOptional = userRepository.findById(idUser);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User with id " + idUser + "not found!");
        }
        return userOptional.get();
    }

    @Override
    public Boolean isFollowed(Long idUserSource, Long idUserFollowed) {
        User userSource = findById(idUserSource);
        User userFollowed = findById(idUserFollowed);

        return userSource.getFollowedUsers().contains(userFollowed);
    }

    @Override
    public Long getNumberFollowers(User user) {
        return userRepository.getNumberFollowing(user.getIdUser());
    }

    @Override
    public List<User> getFollowers(Long idUser) {
        return userRepository.getFollowers(idUser);
    }

}

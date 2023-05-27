package com.example.petfriends.service;

import com.example.petfriends.model.Pet;
import com.example.petfriends.model.User;

import java.util.List;

public interface UserService {

    User save(User user);
    User saveWithoutHash(User user);
    public User findByUsername(String username);

    User findById(Long idUser);
    User getAuthenticatedUser();
    List<User> findByUsernameSearch(String searchInput);
    Boolean isFollowed(Long idUserSource, Long idUserFollowed);

}

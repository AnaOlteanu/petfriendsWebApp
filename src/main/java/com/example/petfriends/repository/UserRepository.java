package com.example.petfriends.repository;

import com.example.petfriends.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(nativeQuery = true, value = "select * from user where lower(username) like concat('%', lower(:searchInput), '%')")
    List<User> findByUsernameSearch(String searchInput);

}

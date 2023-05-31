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

    @Query(nativeQuery = true, value = "select count(id_user_source) from user_follow where id_user_followed = :idUser")
    Long getNumberFollowing(Long idUser);

    List<User> findByIdUserIn(List<Long> userIds);

    @Query(nativeQuery = true, value = "select * from user u join user_follow uf on(u.id_user=uf.id_user_source) where id_user_followed = :idUser")
    List<User> getFollowers(Long idUser);



}

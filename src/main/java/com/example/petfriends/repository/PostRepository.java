package com.example.petfriends.repository;

import com.example.petfriends.model.Post;
import com.example.petfriends.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.user.idUser in :userIds")
    List<Post> findByUserIn(List<Long> userIds);
    List<Post> findByUser(User user);
    @Query(nativeQuery = true, value = "select count(*) from user_like where id_post = :idPost")
    Long getNumberLikes(@Param("idPost") Long idPost);
}

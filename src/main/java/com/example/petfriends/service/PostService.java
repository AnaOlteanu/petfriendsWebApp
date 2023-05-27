package com.example.petfriends.service;

import com.example.petfriends.model.Post;

import java.util.List;

public interface PostService {
    Post save(Post post);
    Post findById(Long idPost);
    void deleteById(Long idPost);
    List<Post> findFromFollowedUsers(Long userId);
    List<Post> findByUserId(Long userId);
    Boolean isLiked(Long idPost, String username);
    Long getNumberLikes(Long idPost);
}

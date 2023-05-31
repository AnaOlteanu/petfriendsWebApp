package com.example.petfriends.service;

import com.example.petfriends.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface PostService {
    Post save(Post post);
    Post findById(Long idPost);
    void deleteById(Long idPost);
    List<Post> findFromFollowedUsers(Long userId);
    List<Post> findByUserId(Long userId);
    Boolean isLiked(Long idPost, String username);
    Long getNumberLikes(Long idPost);

    Page<Post> findPaginatedAndSorted(List<Post> posts, Pageable pageable);
}

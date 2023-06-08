package com.example.petfriends.service.impl;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.Post;
import com.example.petfriends.model.User;
//import com.example.petfriends.model.UserFollow;
import com.example.petfriends.repository.PostRepository;
//import com.example.petfriends.repository.UserFollowRepository;
import com.example.petfriends.repository.UserRepository;
import com.example.petfriends.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public Post findById(Long idPost) {
        Optional<Post> postOptional = postRepository.findById(idPost);
        if(postOptional.isEmpty()) {
            log.info("Error when trying to find the post with id {}", idPost);
            throw new NotFoundException("Post with id " + idPost + " not found!");
        }
        log.info("Returned post with id {}", idPost);
        return postOptional.get();
    }

    @Override
    public void deleteById(Long idPost) {
        Optional<Post> postOptional = postRepository.findById(idPost);
        if(postOptional.isEmpty()) {
            throw new NotFoundException("Post with id: " + idPost + " not found");
        }

        Post post = postOptional.get();
        postRepository.save(post);
        postRepository.deleteById(idPost);
        log.info("Successfully deleted post with id {}", idPost);
    }

    @Override
    public List<Post> findFromFollowedUsers(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()){
            throw new NotFoundException("User with id {} " + userId + " not found");
        }
        List<User> followedUsers = user.get().getFollowedUsers();
        List<Long> followedUserIds = followedUsers.stream()
                .map(User::getIdUser)
                .collect(Collectors.toList());

        return postRepository.findByUserIn(followedUserIds);

    }

    @Override
    public List<Post> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id:" + userId + " does not exist!"));
        List<Post> posts = postRepository.findByUser(user);

        return posts;
    }

    @Override
    public Boolean isLiked(Long idPost, String username) {
        Post post = findById(idPost);
        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new NotFoundException("User with username: " +  username + " not found");
        }
        return userOptional.get().getLikedPosts().contains(post);
    }

    @Override
    public Long getNumberLikes(Long idPost) {
        return postRepository.getNumberLikes(idPost);
    }

    @Override
    public Page<Post> findPaginatedAndSorted(List<Post> posts, Pageable pageable) {

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        List<Post> sortedPosts = sortPosts(posts, pageable.getSort());

        if (sortedPosts.size() < startItem) {
            return Page.empty();
        }

        List<Post> pagedPosts = new ArrayList<>();
        if (sortedPosts.size() >= startItem + pageSize) {
            pagedPosts = sortedPosts.subList(startItem, startItem + pageSize);
        } else {
            pagedPosts = sortedPosts.subList(startItem, sortedPosts.size());
        }

        return new PageImpl<>(pagedPosts, pageable, sortedPosts.size());

    }

    private List<Post> sortPosts(List<Post> posts, Sort sort) {
        List<Sort.Order> orders = sort.get().collect(Collectors.toList());

        if (!orders.isEmpty()) {
            Comparator<Post> comparator = Comparator.comparing(Post::getDate).reversed();
            posts.sort(comparator);
        }

        return posts;
    }




}

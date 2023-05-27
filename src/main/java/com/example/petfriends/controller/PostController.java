package com.example.petfriends.controller;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.Comment;
import com.example.petfriends.model.Post;
import com.example.petfriends.model.User;
import com.example.petfriends.service.ImageService;
import com.example.petfriends.service.PostService;
import com.example.petfriends.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @Autowired
    private ImageService imageService;

    @GetMapping("/post/list")
    public ModelAndView listPosts(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);

        User authenticatedUser = userService.getAuthenticatedUser();
        List<Post> posts = postService.findFromFollowedUsers(authenticatedUser.getIdUser());

        log.info("Posts from followed people {} ", posts);

        ModelAndView modelAndView = new ModelAndView("post-list");
        modelAndView.addObject("posts", posts);
        modelAndView.addObject("user", authenticatedUser);
        modelAndView.addObject("post", new Post());

        return modelAndView;

    }

    @PostMapping("/post")
    public String addPost(@Valid @ModelAttribute("post") Post post,
                          BindingResult bindingResult,
                          @RequestParam("postimages")MultipartFile[] images,
                          Model model) throws IOException {

        if(bindingResult.hasErrors()){
            User authenticatedUser = userService.getAuthenticatedUser();
            List<Post> posts = postService.findFromFollowedUsers(authenticatedUser.getIdUser());
            model.addAttribute("posts", posts);
            model.addAttribute("user", authenticatedUser);
            return "post-list";
        }

        post.setDate(LocalDateTime.now());
        post.setUser(userService.getAuthenticatedUser());
        Post savedPost = postService.save(post);
        log.info("image length {}", images.length);
        if(images.length > 0) {
            imageService.savePostImageFile(savedPost.getIdPost(), images);
        }

        return "redirect:/user/" + post.getUser().getUsername();
    }

    @GetMapping("/post/{idPost}")
    public String getPostById(@PathVariable("idPost") Long idPost, Model model){
        User authenticatedUser = userService.getAuthenticatedUser();
        model.addAttribute("post", postService.findById(idPost));
//        model.addAttribute("lastPosted", postService.findLastPosted());
        model.addAttribute("isLiked", postService.isLiked(idPost,authenticatedUser.getUsername()));
        model.addAttribute("numberLikes", postService.getNumberLikes(idPost));
        model.addAttribute("comment", new Comment());
        return "post-single";
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping("/post/delete/{idPost}")
    public String deletePostById(@PathVariable("idPost") Long idPost,
                             @RequestParam("username") String username){
        postService.deleteById(idPost);
        log.info("User {} successfully deleted post with id {}", username, idPost);
        return "redirect:/user/" + username;
    }

    @GetMapping("/post/edit/{idPost}")
    public ModelAndView editPostById(@PathVariable("idPost") Long idPost) {
        User authenticatedUser = userService.getAuthenticatedUser();
        Post post = postService.findById(idPost);
        if(!authenticatedUser.equals(post.getUser())) {
            ModelAndView modelAndView = new ModelAndView("access-denied");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("post-edit");
        modelAndView.addObject("post", post);
        return modelAndView;
    }
    
    @PostMapping("/post/edit")
    public String editPost(@ModelAttribute @Valid Post post,
                             BindingResult bindingResult,
                             @RequestParam("postimages") MultipartFile[] images) throws IOException {

        if (bindingResult.hasErrors()) {
            return "post-edit";
        }
        Post dbPost = postService.findById(post.getIdPost());
        post.setUser(dbPost.getUser());
        post.getImages().addAll(dbPost.getImages());
        post.setDate(LocalDateTime.now());
        Post savedPost = postService.save(post);
        log.info("image length {}", images.length);
        if(images.length > 1) {
            imageService.savePostImageFile(savedPost.getIdPost(), images);
        }
        log.info("Successfully edited post with id {}", savedPost.getIdPost());
        return "redirect:/post/" + post.getIdPost();
    }

    @RequestMapping("/like/{idPost}/user/{username}")
    public String likePost(@PathVariable("idPost") Long idPost,
                           @PathVariable("username") String username) {
        Post post = postService.findById(idPost);
        User user = userService.findByUsername(username);

        user.getLikedPosts().add(post);
        User savedUser = userService.saveWithoutHash(user);
        log.info("User {} liked post with id {}", username, idPost);
        return "redirect:/post/" + idPost;
    }

    @RequestMapping("/removeLike/{idPost}/user/{username}")
    public String removeLikePost(@PathVariable("idPost") Long idPost,
                                 @PathVariable("username") String username) {
        Post blog = postService.findById(idPost);
        User user = userService.findByUsername(username);

        if(postService.isLiked(idPost, username)) {
            user.getLikedPosts().remove(blog);
            userService.saveWithoutHash(user);
        } else {
            throw new NotFoundException("Like not found!");
        }
        log.info("User {} removed like from post with id {}", username, idPost);
        return "redirect:/post/" + idPost;

    }



}

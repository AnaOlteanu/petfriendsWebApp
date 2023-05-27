package com.example.petfriends.controller;

import com.example.petfriends.model.Comment;
import com.example.petfriends.model.Post;
import com.example.petfriends.model.User;
import com.example.petfriends.service.CommentService;
import com.example.petfriends.service.PostService;
import com.example.petfriends.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/comment/{idPost}/user/{username}")
    public String addComment(@ModelAttribute("comment") @Valid Comment comment,
                             BindingResult bindingResult,
                             @PathVariable("idPost") Long idPost,
                             @PathVariable("username") String username,
                             Model model){

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", postService.findById(idPost));
            return "post-single";
        }

        Post post = postService.findById(idPost);
        User user = userService.findByUsername(username);

        comment.setPost(post);
        comment.setUser(user);
        comment.setDate(LocalDateTime.now());
        commentService.save(comment);

        return "redirect:/post/" + idPost;

    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @RequestMapping("/comment/delete/{idComment}")
    public String deleteById(@PathVariable("idComment") Long idComment,
                             @RequestParam("idPost") Long idPost,
                             @RequestParam("username") String username){
        commentService.deleteById(idComment);

        return "redirect:/post/" + idPost;
    }

}

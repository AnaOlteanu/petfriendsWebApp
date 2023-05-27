package com.example.petfriends.service.impl;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.Comment;
import com.example.petfriends.repository.CommentRepository;
import com.example.petfriends.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;


    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long idComment) {
        Optional<Comment> commentOptional = commentRepository.findById(idComment);
        if (commentOptional.isEmpty()) {
            throw new NotFoundException("Comment with id " + idComment + " not found");
        }
        commentRepository.delete(commentOptional.get());
    }
}

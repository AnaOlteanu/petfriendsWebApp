package com.example.petfriends.service;

import com.example.petfriends.model.Comment;

public interface CommentService {

    Comment save(Comment comment);

    void deleteById(Long idComment);
}

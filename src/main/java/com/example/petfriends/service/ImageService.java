package com.example.petfriends.service;

import com.example.petfriends.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ImageService {

     void saveUserImageFile(Long userId, MultipartFile[] images) throws IOException;
     void savePetImageFile(Long petId, MultipartFile[] images) throws IOException;

     void savePostImageFile(Long postId, MultipartFile[] images) throws IOException;

     void saveEventImageFile(Long eventId, MultipartFile[] images) throws IOException;

     Optional<Image> findById(Long id);
}

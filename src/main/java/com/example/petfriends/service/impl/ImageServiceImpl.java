package com.example.petfriends.service.impl;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.*;
import com.example.petfriends.repository.*;
import com.example.petfriends.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PetRepository petRepository;
    private final PostRepository postRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public void saveUserImageFile(Long userId, MultipartFile[] images) throws IOException {

        Optional<User> user = userRepository.findById(userId);

        if(user.isPresent()) {
            List<Image> pictures = user.get().getImages();

            if(pictures == null || pictures.size() == 0) {
                pictures = new ArrayList<>();
            }

            for (MultipartFile file: images) {
                Byte[] byteObjects = new Byte[file.getBytes().length];
                int i = 0; for (byte b : file.getBytes()){
                    byteObjects[i++] = b; }

                Image picture = new Image();
                picture.setImage(byteObjects);
                picture.setUser(user.get());
                Image savedImage = imageRepository.save(picture);
                pictures.add(savedImage);
                user.get().setImages(pictures);
                userRepository.save(user.get());
            }

        } else {
            throw new NotFoundException("User with id " + userId + " not found");
        }

    }

    @Override
    @Transactional
    public void savePetImageFile(Long petId, MultipartFile[] images) throws IOException {

        Optional<Pet> pet = petRepository.findById(petId);

        if(pet.isPresent()) {
            List<Image> pictures = pet.get().getImages();

            if(pictures == null || pictures.size() == 0) {
                pictures = new ArrayList<>();
            }

            for (MultipartFile file: images) {
                Byte[] byteObjects = new Byte[file.getBytes().length];
                int i = 0; for (byte b : file.getBytes()){
                    byteObjects[i++] = b; }

                Image picture = new Image();
                picture.setImage(byteObjects);
                picture.setPet(pet.get());
                Image savedImage = imageRepository.save(picture);
                pictures.add(savedImage);
                pet.get().setImages(pictures);
                petRepository.save(pet.get());
            }

        } else {
            throw new NotFoundException("Pet with id " + petId + " not found");
        }

    }

    @Override
    @Transactional
    public void savePostImageFile(Long postId, MultipartFile[] images) throws IOException {
        Optional<Post> post = postRepository.findById(postId);

        if(post.isPresent()) {
            List<Image> pictures = post.get().getImages();

            if(pictures == null || pictures.size() == 0) {
                pictures = new ArrayList<>();
            }

            for (MultipartFile file: images) {
                Byte[] byteObjects = new Byte[file.getBytes().length];
                int i = 0; for (byte b : file.getBytes()){
                    byteObjects[i++] = b; }

                Image picture = new Image();
                picture.setImage(byteObjects);
                picture.setPost(post.get());
                Image savedImage = imageRepository.save(picture);
                pictures.add(savedImage);
                post.get().setImages(pictures);
                postRepository.save(post.get());
            }

        } else {
            throw new NotFoundException("Post with id " + postId + " not found");
        }
    }

    @Override
    @Transactional
    public void saveEventImageFile(Long eventId, MultipartFile[] images) throws IOException {

        Optional<Event> event = eventRepository.findById(eventId);

        if(event.isPresent()) {
            List<Image> pictures = event.get().getImages();

            if(pictures == null || pictures.size() == 0) {
                pictures = new ArrayList<>();
            }

            for (MultipartFile file: images) {
                Byte[] byteObjects = new Byte[file.getBytes().length];
                int i = 0; for (byte b : file.getBytes()){
                    byteObjects[i++] = b; }

                Image picture = new Image();
                picture.setImage(byteObjects);
                picture.setEvent(event.get());
                Image savedImage = imageRepository.save(picture);
                pictures.add(savedImage);
                event.get().setImages(pictures);
                eventRepository.save(event.get());
            }

        } else {
            throw new NotFoundException("Event with id " + eventId + " not found");
        }

    }

    @Override
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

}

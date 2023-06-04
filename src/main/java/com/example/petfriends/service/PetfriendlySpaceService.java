package com.example.petfriends.service;

import com.example.petfriends.model.PetfriendlySpace;

import java.util.List;

public interface PetfriendlySpaceService {
    void save(PetfriendlySpace petfriendlySpace);
    List<PetfriendlySpace> getAllPetfriendlySpaces();
}

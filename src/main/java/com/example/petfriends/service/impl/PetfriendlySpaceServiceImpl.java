package com.example.petfriends.service.impl;

import com.example.petfriends.model.PetfriendlySpace;
import com.example.petfriends.repository.PetfriendlySpaceRepository;
import com.example.petfriends.service.PetfriendlySpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetfriendlySpaceServiceImpl implements PetfriendlySpaceService {
    @Autowired
    private PetfriendlySpaceRepository petfriendlySpaceRepository;
    @Override
    public void save(PetfriendlySpace petfriendlySpace) {
        petfriendlySpaceRepository.save(petfriendlySpace);
    }

    @Override
    public List<PetfriendlySpace> getAllPetfriendlySpaces() {
        return petfriendlySpaceRepository.findAll();
    }
}

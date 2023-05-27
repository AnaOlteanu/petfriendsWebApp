package com.example.petfriends.service.impl;

import com.example.petfriends.model.Pet;
import com.example.petfriends.repository.PetRepository;
import com.example.petfriends.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetServiceImpl implements PetService {

    @Autowired
    private PetRepository petRepository;

    @Override
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }
}

package com.example.petfriends.service.impl;

import com.example.petfriends.model.Petshop;
import com.example.petfriends.repository.PetshopRepository;
import com.example.petfriends.service.PetshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetshopServiceImpl implements PetshopService {
    @Autowired
    private PetshopRepository petshopRepository;

    @Override
    public List<Petshop> findAll() {
        return petshopRepository.findAll();
    }

    @Override
    public Petshop save(Petshop petshop) {
        return petshopRepository.save(petshop);
    }
}

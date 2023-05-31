package com.example.petfriends.service.impl;

import com.example.petfriends.model.City;
import com.example.petfriends.model.Petshop;
import com.example.petfriends.repository.CityRepository;
import com.example.petfriends.repository.PetshopRepository;
import com.example.petfriends.service.CityService;
import com.example.petfriends.service.PetshopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetshopServiceImpl implements PetshopService {
    @Autowired
    private PetshopRepository petshopRepository;

    @Autowired
    private CityService cityService;

    @Override
    public List<Petshop> findAll() {
        return petshopRepository.findAll();
    }

    @Override
    public Petshop save(Petshop petshop) {
        return petshopRepository.save(petshop);
    }

    @Override
    public List<Petshop> findByCity(City city) {
        return petshopRepository.findAllByCity(city);

    }

}

package com.example.petfriends.service;

import com.example.petfriends.model.City;
import com.example.petfriends.model.Petshop;

import java.util.List;

public interface PetshopService {
    List<Petshop> findAll();
    Petshop save(Petshop petshop);
    List<Petshop> findByCity(City city);
    void deleteById(Long idPetshop);
    Petshop findById(Long idPetshop);
}

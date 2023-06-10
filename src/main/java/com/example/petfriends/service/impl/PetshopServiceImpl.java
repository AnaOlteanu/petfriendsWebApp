package com.example.petfriends.service.impl;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.City;
import com.example.petfriends.model.Pet;
import com.example.petfriends.model.Petshop;
import com.example.petfriends.model.Post;
import com.example.petfriends.repository.CityRepository;
import com.example.petfriends.repository.PetshopRepository;
import com.example.petfriends.service.CityService;
import com.example.petfriends.service.PetshopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
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

    @Override
    public void deleteById(Long idPetshop) {
        Optional<Petshop> petshopOptional = petshopRepository.findById(idPetshop);
        if(petshopOptional.isEmpty()) {
            throw new NotFoundException("Petshop with id: " + idPetshop + " not found");
        }

        Petshop petshop = petshopOptional.get();
        petshopRepository.save(petshop);
        petshopRepository.deleteById(idPetshop);
        log.info("Successfully deleted petshop with id {}", idPetshop);
    }

    @Override
    public Petshop findById(Long idPetshop) {
        Optional<Petshop> petshopOptional = petshopRepository.findById(idPetshop);
        if(petshopOptional.isEmpty()) {
            log.info("Error when trying to find the petshop with id {}", idPetshop);
            throw new NotFoundException("Petshop with id " + idPetshop + " not found!");
        }
        log.info("Returned petshop with id {}", idPetshop);
        return petshopOptional.get();
    }

}

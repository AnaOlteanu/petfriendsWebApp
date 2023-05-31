package com.example.petfriends.service.impl;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.City;
import com.example.petfriends.model.User;
import com.example.petfriends.repository.CityRepository;
import com.example.petfriends.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;

    @Override
    public Optional<City> findById(Long idCity) {
        return cityRepository.findById(idCity);
    }

    @Override
    public Optional<City> findByName(String name) {
        return cityRepository.findByCityName(name);
    }



    @Override
    public List<City> findAllCities() {
        return cityRepository.findAll();
    }
}

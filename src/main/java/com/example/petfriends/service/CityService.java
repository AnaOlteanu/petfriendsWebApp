package com.example.petfriends.service;

import com.example.petfriends.model.City;

import java.util.List;
import java.util.Optional;

public interface CityService {

    Optional<City> findById(Long idCity);
    Optional<City> findByName(String name);

    List<City> findAllCities();
}

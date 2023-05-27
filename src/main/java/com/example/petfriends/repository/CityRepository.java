package com.example.petfriends.repository;

import com.example.petfriends.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}

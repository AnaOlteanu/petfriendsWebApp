package com.example.petfriends.repository;

import com.example.petfriends.model.City;
import com.example.petfriends.model.Petshop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PetshopRepository extends JpaRepository<Petshop, Long> {

    List<Petshop> findAllByCity(City city);
}

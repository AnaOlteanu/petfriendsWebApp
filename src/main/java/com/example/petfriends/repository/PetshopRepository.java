package com.example.petfriends.repository;

import com.example.petfriends.model.Petshop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetshopRepository extends JpaRepository<Petshop, Long> {
}

package com.example.petfriends.repository;

import com.example.petfriends.model.Role;
import com.example.petfriends.model.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum role);
}

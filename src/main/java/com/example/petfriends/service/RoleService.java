package com.example.petfriends.service;

import com.example.petfriends.model.Role;
import com.example.petfriends.model.RoleEnum;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAllRoles();

    Optional<Role> findById(Long id);

    Role findByRoleName(RoleEnum role);
}

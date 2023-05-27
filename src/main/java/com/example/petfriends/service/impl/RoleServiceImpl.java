package com.example.petfriends.service.impl;

import com.example.petfriends.model.Role;
import com.example.petfriends.model.RoleEnum;
import com.example.petfriends.repository.RoleRepository;
import com.example.petfriends.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.petfriends.model.RoleEnum.ROLE_EVENT_PLANNER;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role findByRoleName(RoleEnum role) {
        return roleRepository.findByName(role);
    }
}

package com.example.petfriends.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "role")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_role")
    private Long idRole;

    @Column(columnDefinition = "enum('ROLE_USER', 'ROLE_EVENT_PLANNER', 'ROLE_ADMIN')")
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Override
    public String toString() {
        return getName().name();
    }

    public Role(RoleEnum roleUser) {
        this.name = roleUser;
    }

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<User> users;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Admin> admins;
}

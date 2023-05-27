package com.example.petfriends.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long idUser;

    @NotEmpty(message = "Username cannot be empty!")
    @Length(min = 3, message = "Username should be at least 3 characters!")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Password cannot be empty!")
    @Length(min = 5, message = "Password should be at least 5 characters!")
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "admin_roles",
            joinColumns = @JoinColumn(name = "id_admin"),
            inverseJoinColumns = @JoinColumn(name = "id_role"))
    private Set<Role> roles;
}

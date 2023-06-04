package com.example.petfriends.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "petfriendlyspace")
public class PetfriendlySpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet_space")
    private Long idPetSpace;

    @NotEmpty(message = "Name cannot be empty!")
    private String name;

    @NotEmpty(message = "Description cannot be empty!")
    private String description;

    @NotEmpty(message = "Description cannot be empty!")
    private String address;

    private double latitude;
    private double longitude;

}

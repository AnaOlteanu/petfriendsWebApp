package com.example.petfriends.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.format.DateTimeFormatter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Petshop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet_shop")
    private Long idPetShop;

    @NotEmpty(message = "Petshop name cannot be empty!")
    private String name;

    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Invalid time format. Expected format: HH:mm")
    private String openingTime;

    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Invalid time format. Expected format: HH:mm")
    private String closingTime;

    @NotEmpty(message = "Address cannot be empty!")
    private String address;

    @NotEmpty(message = "Phone number cannot be empty!")
    @Column(unique = true)
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "id_city", nullable = false)
    private City city;

}

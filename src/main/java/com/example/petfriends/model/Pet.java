package com.example.petfriends.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pet")
    private Long idPet;

    @NotEmpty(message = "Pet name cannot be empty!")
    private String name;

    @NotNull(message = "Pet age cannot be empty!")
    @Range(min = 1)
    private Integer age;

    @Column(columnDefinition = "enum('DOG', 'CAT', 'PARROT', 'RABBIT', 'HAMSTER')")
    @Enumerated(EnumType.STRING)
    private AnimalType type;

    @NotEmpty(message = "Pet breed cannot be empty!")
    private String breed;

    @Column(columnDefinition = "enum('F', 'M')")
    @NotNull(message = "Pet gender cannot be empty!")
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @OneToMany(mappedBy = "pet")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Image> images = new ArrayList<>();

    public Pet(String name, Integer age, AnimalType type, String breed, GenderEnum gender) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.breed = breed;
        this.gender = gender;
    }
}

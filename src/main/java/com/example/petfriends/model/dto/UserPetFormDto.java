package com.example.petfriends.model.dto;

import com.example.petfriends.model.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserPetFormDto {

    @NotEmpty(message = "Username cannot be empty!")
    @Length(min = 3, message = "Username should be at least 3 characters!")
    private String username;

    @NotEmpty(message = "Password cannot be empty!")
    @Length(min = 5, message = "Password should be at least 5 characters!")
    private String password;

    @NotEmpty(message = "First name cannot be empty!")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty!")
    private String lastName;

    @Email(message = "Email is not valid!")
    @NotEmpty(message = "Email cannot be empty!")
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "Birth date should be in the past!")
    private LocalDate birthDate;


    @NotEmpty(message = "Pet name cannot be empty!")
    private String petName;

    @NotNull(message = "Pet age cannot be empty!")
    @Range(min = 1)
    private Integer petAge;

    @Enumerated(EnumType.STRING)
    private AnimalType petType;

    @NotEmpty(message = "Pet breed cannot be empty!")
    private String petBreed;

    @Enumerated(EnumType.STRING)
    private GenderEnum petGender;

    private City city;

    public UserPetFormDto() {
    }


}

package com.example.petfriends.model.mapper;

import com.example.petfriends.model.Pet;
import com.example.petfriends.model.User;
import com.example.petfriends.model.dto.UserPetFormDto;
import org.springframework.stereotype.Component;

@Component
public class UserPetFormMapper {

    public User userPetFormDtoToUser(UserPetFormDto userPetFormDto){
        return new User(userPetFormDto.getFirstName(),
                userPetFormDto.getLastName(),
                userPetFormDto.getEmail(),
                userPetFormDto.getUsername(),
                userPetFormDto.getPassword(),
                userPetFormDto.getBirthDate(),
                userPetFormDto.getCity());
    }

    public Pet userPetFormDtoToPet(UserPetFormDto userPetFormDto){
        return new Pet(userPetFormDto.getPetName(),
                userPetFormDto.getPetAge(),
                userPetFormDto.getPetType(),
                userPetFormDto.getPetBreed(),
                userPetFormDto.getPetGender());
    }
}

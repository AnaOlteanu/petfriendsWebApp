package com.example.petfriends.model.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
public class EventPreprocessed {
    private Long idEvent;
    private String title;
    private String description;
    private String category;
    private String location;
    private String city;

}

package com.example.petfriends.model;

public enum CategoryEnum {
    PLAYDATE("PLAYDATE"),
    MEETING_FOR_ALL("MEETING FOR ALL"),
    ADOPTION("ADOPTION"),
    FAMILY_MEETING("FAMILY MEETING"),
    BREED_MEETING("BREED MEETING");

    private String description;

    public String getDescription() {
        return description;
    }

    CategoryEnum(String description){ this.description = description;}
}

package com.example.petfriends.model;

public enum GenderEnum {
    F("Female"), M("Male");

    private String description;

    public String getDescription() {
        return description;
    }

    GenderEnum(String description){ this.description = description;}
}

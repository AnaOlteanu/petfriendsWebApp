package com.example.petfriends.service;

import com.example.petfriends.model.Event;
import com.example.petfriends.model.User;

import java.util.List;
import java.util.Optional;

public interface EventService {
    Event save(Event event);
    Event findById(Long idEvent);
    List<Event> getCurrentEvents();
    List<Event> getEndedEvents();
    List<Event> getFutureEvents();
    List<User> findPeopleJoined(Event event);
    List<Event> findAll();
    void deleteById(Long idPost);
}

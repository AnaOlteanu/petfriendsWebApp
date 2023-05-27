package com.example.petfriends.service;

import com.example.petfriends.model.Event;

import java.util.List;

public interface EventService {
    List<Event> getCurrentEvents();
    List<Event> getEndedEvents();
}

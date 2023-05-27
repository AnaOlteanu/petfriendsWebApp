package com.example.petfriends.service.impl;

import com.example.petfriends.model.Event;
import com.example.petfriends.repository.EventRepository;
import com.example.petfriends.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Override
    public List<Event> getCurrentEvents() {
        LocalDate currentDate = LocalDate.now();
        return eventRepository.findCurrentEvents(currentDate, currentDate);
    }

    @Override
    public List<Event> getEndedEvents() {
        LocalDate currentDate = LocalDate.now();
        return eventRepository.findAllByEndDateBefore(currentDate);
    }
}

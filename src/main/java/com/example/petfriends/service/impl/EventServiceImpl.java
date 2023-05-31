package com.example.petfriends.service.impl;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.Event;
import com.example.petfriends.model.Post;
import com.example.petfriends.model.User;
import com.example.petfriends.repository.EventRepository;
import com.example.petfriends.repository.UserRepository;
import com.example.petfriends.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event findById(Long idEvent) {
        Optional<Event> eventOptional = eventRepository.findById(idEvent);
        if(eventOptional.isEmpty()) {
            log.info("Error when trying to find the blog with id {}", idEvent);
            throw new NotFoundException("Event with id " + idEvent + "not found!");
        }
        log.info("Returned event with id {}", idEvent);
        return eventOptional.get();
    }

    @Override
    public List<Event> getCurrentEvents() {
        LocalDate currentDate = LocalDate.now();
        return eventRepository.findCurrentEvents(currentDate);
    }

    @Override
    public List<Event> getEndedEvents() {
        LocalDate currentDate = LocalDate.now();
        return eventRepository.findAllByEndDateBefore(currentDate);
    }

    @Override
    public List<Event> getFutureEvents() {
        LocalDate currentDate = LocalDate.now();
        return eventRepository.findFutureEvents(currentDate);
    }

    @Override
    public List<User> findPeopleJoined(Event event) {
        List<Long> idUsersJoined = eventRepository.findPeopleJoined(event.getIdEvent());
        List<User> usersJoined = userRepository.findByIdUserIn(idUsersJoined);

        return usersJoined;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    public void deleteById(Long idPost) {
        Optional<Event> eventOptional = eventRepository.findById(idPost);
        if(eventOptional.isEmpty()) {
            throw new NotFoundException("Event with id: " + idPost + "not found");
        }

        Event event = eventOptional.get();
        eventRepository.save(event);
        eventRepository.deleteById(idPost);
        log.info("Successfully deleted post with id {}", idPost);
    }
}

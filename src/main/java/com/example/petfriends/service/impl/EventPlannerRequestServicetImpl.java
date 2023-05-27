package com.example.petfriends.service.impl;

import com.example.petfriends.exception.NotFoundException;
import com.example.petfriends.model.EventPlannerRequest;
import com.example.petfriends.model.Post;
import com.example.petfriends.model.User;
import com.example.petfriends.repository.EventPlannerRequestRepository;
import com.example.petfriends.service.EventPlannerRequestService;
import com.example.petfriends.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.petfriends.model.RequestStatus.PENDING;

@Service
@Slf4j
public class EventPlannerRequestServicetImpl implements EventPlannerRequestService {
    @Autowired
    private EventPlannerRequestRepository eventPlannerRequestRepository;

    @Autowired
    private UserService userService;
    @Override
    public EventPlannerRequest createRequest(User user) {
        EventPlannerRequest request = new EventPlannerRequest();
        request.setUser(user);
        request.setStatus(PENDING);
        return eventPlannerRequestRepository.save(request);
    }

    @Override
    public EventPlannerRequest findRequestByUsername(String username) {
        User user = userService.findByUsername(username);
        EventPlannerRequest request = eventPlannerRequestRepository.findByUser(user);
        return request;
    }

    @Override
    public List<EventPlannerRequest> findAll() {
        return eventPlannerRequestRepository.findAll();
    }

    @Override
    public EventPlannerRequest findById(Long idRequest) {
        Optional<EventPlannerRequest> requestOptional = eventPlannerRequestRepository.findById(idRequest);
        if(requestOptional.isEmpty()) {
            log.info("Error when trying to find the request with id {}", idRequest);
            throw new NotFoundException("Request with id " + idRequest + "not found!");
        }
        log.info("Returned requesr with id {}", idRequest);
        return requestOptional.get();
    }

    @Override
    public EventPlannerRequest save(EventPlannerRequest request) {
        return eventPlannerRequestRepository.save(request);
    }
}

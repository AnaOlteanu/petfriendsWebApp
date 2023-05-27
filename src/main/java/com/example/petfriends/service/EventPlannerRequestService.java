package com.example.petfriends.service;

import com.example.petfriends.model.EventPlannerRequest;
import com.example.petfriends.model.User;

import java.util.List;

public interface EventPlannerRequestService {
    EventPlannerRequest createRequest(User user);
    EventPlannerRequest findRequestByUsername(String username);
    List<EventPlannerRequest> findAll();

    EventPlannerRequest findById(Long idRequest);

    EventPlannerRequest save(EventPlannerRequest request);


}

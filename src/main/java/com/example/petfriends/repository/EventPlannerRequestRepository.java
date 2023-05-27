package com.example.petfriends.repository;

import com.example.petfriends.model.EventPlannerRequest;
import com.example.petfriends.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPlannerRequestRepository extends JpaRepository<EventPlannerRequest, Long> {
    EventPlannerRequest findByUser(User user);
}

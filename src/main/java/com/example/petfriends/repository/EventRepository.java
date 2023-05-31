package com.example.petfriends.repository;

import com.example.petfriends.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByEndDateBefore(LocalDate date);

    @Query(nativeQuery = true, value = "select * from event where YEAR(start_date) <= YEAR(:date) and MONTH(start_date) <= MONTH(:date) and DAY(start_date) <= DAY(:date) and end_date >= (:date);")
    List<Event> findCurrentEvents(LocalDate date);

    @Query(nativeQuery = true, value = "select * from event where start_date > :date")
    List<Event> findFutureEvents(LocalDate date);

    @Query(nativeQuery = true, value = "select id_user from user_event where id_event = :idEvent")
    List<Long> findPeopleJoined(Long idEvent);
}

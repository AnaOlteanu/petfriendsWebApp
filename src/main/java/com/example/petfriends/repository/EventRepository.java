package com.example.petfriends.repository;

import com.example.petfriends.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByEndDateBefore(LocalDate date);

    @Query(nativeQuery = true, value = "select * from event where start_date <= :date1 and end_date > :date2")
    List<Event> findCurrentEvents(LocalDate date1, LocalDate date2);
}

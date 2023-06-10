package com.example.petfriends.model.recommendation;

import com.example.petfriends.model.Event;
import com.example.petfriends.model.Pet;
import com.example.petfriends.model.User;
import com.example.petfriends.service.EventService;
import com.example.petfriends.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventRecommender {



    @Autowired
    private UserService userService;


    public List<Event> generateEventRecommendations(Long idUser, int numRecommendations, List<Event> events) {
        User user = userService.findById(idUser);
        Set<Event> userEvents = user.getEventsJoined();

        List<Event> allEvents = new ArrayList<>(events);

        Pet pet = user.getPet();

        List<Long> eventIds = new ArrayList<>();

        for (Event event : userEvents) {
            eventIds.add(event.getIdEvent());
        }

        EventPreprocessor eventPreprocessor = new EventPreprocessor();
        TFIDFModel tfidfModel = new TFIDFModel();
        UserProfileModel userProfileModel = new UserProfileModel();
        SimilarityCalculator similarityCalculator = new SimilarityCalculator();


        List<EventPreprocessed> eventPreprocessedList = eventPreprocessor.preprocessEvents(allEvents);

        Map<Long, Map<String, Double>> tfidfMap = tfidfModel.calculateTFIDF(eventPreprocessedList, pet.getBreed());

        for(Map.Entry<Long, Map<String, Double>> entry : tfidfMap.entrySet()) {
            log.info("EVENT RECOMMENDER TFIDF MAP {}", entry);
        };

        Map<String, Double> userProfile = userProfileModel.calculateUserProfile(eventIds,tfidfMap,pet.getBreed());

        Map<Long, Double> eventUserSimilarities = similarityCalculator.calculateSimilarities(userProfile, tfidfMap);

        log.info("Event user similarities {} ", eventUserSimilarities);

        List<Event> recommendations = eventUserSimilarities.entrySet().stream()
                .filter(entry -> !eventIds.contains(entry.getKey()))
                .filter(entry -> entry.getValue() > 0)
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(numRecommendations)
                .map(entry -> getEventById(entry.getKey(), allEvents))
                .collect(Collectors.toList());

        return recommendations;
    }

    private Event getEventById(Long eventId, List<Event> events) {
        for (Event event : events) {
            if (event.getIdEvent().equals(eventId)) {
                return event;
            }
        }
        return null;
    }
}

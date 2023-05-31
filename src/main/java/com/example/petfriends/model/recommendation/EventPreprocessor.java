package com.example.petfriends.model.recommendation;

import com.example.petfriends.model.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
public class EventPreprocessor {
    public List<EventPreprocessed> preprocessEvents(List<Event> events) {
        return events.stream()
                .map(this::preprocessEvent)
                .collect(Collectors.toList());
    }

    private EventPreprocessed preprocessEvent(Event event) {
        String preprocessedTitle = TextProcessor.preprocessText(event.getTitle());
        String preprocessedDescription = TextProcessor.preprocessText(event.getDescription());
        String preprocessedCategory = TextProcessor.preprocessText(event.getCategory().toString());
        String preprocessedLocation = TextProcessor.preprocessText(event.getLocation());
        String preprocessedCity = TextProcessor.preprocessText(event.getCity().getCityName());

        EventPreprocessed eventPreprocessed = new EventPreprocessed(
                event.getIdEvent(),
                preprocessedTitle,
                preprocessedDescription,
                preprocessedCategory,
                preprocessedLocation,
                preprocessedCity
        );

        log.info("Event preprocessed {} ", eventPreprocessed.toString());

        return eventPreprocessed;
    }
}

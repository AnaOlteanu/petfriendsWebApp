package com.example.petfriends.model.recommendation;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SimilarityCalculator {
    public Map<Long, Double> calculateSimilarities(Map<String, Double> userProfile, Map<Long, Map<String, Double>> tfidfMap) {
        Map<Long, Double> similarities = new HashMap<>();
        log.info("========CALCULATE SIMILARITIES========");

        for (Map.Entry<Long, Map<String, Double>> entry : tfidfMap.entrySet()) {
            Long eventId = entry.getKey();
            Map<String, Double> eventTFIDF = entry.getValue();
            log.info("event id {} ", eventId);
            log.info("event tfidf {}", eventTFIDF);

            double similarity = calculateCosineSimilarity(userProfile, eventTFIDF);

            similarities.put(eventId, similarity);
        }

        return similarities;
    }

    private double calculateCosineSimilarity(Map<String, Double> userProfile, Map<String, Double> eventTFIDF) {
        double dotProduct = 0.0;
        double userNorm = 0.0;
        double eventNorm = 0.0;

        log.info("========COSINE========");

        for (Map.Entry<String, Double> userProfileEntry : userProfile.entrySet()) {
            String term = userProfileEntry.getKey();
            double userProfileValue = userProfileEntry.getValue();
            double eventValue = eventTFIDF.getOrDefault(term, 0.0);

            log.info("term user profile {}", term);
            log.info("term value user profile {}", userProfileValue);
            log.info("event value {}", eventValue);

            dotProduct += userProfileValue * eventValue;
            userNorm += Math.pow(userProfileValue, 2);
            eventNorm += Math.pow(eventValue, 2);

            log.info("user norm {}", userNorm);
            log.info("event norm {}", eventNorm);
        }

        userNorm = Math.sqrt(userNorm);
        eventNorm = Math.sqrt(eventNorm);


        if (userNorm > 0 && eventNorm > 0) {
            log.info("term score {}", dotProduct / (userNorm * eventNorm));
            return dotProduct / (userNorm * eventNorm);
        } else {
            return 0.0;
        }
    }
}

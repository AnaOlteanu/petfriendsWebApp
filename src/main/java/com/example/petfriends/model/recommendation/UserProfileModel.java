package com.example.petfriends.model.recommendation;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserProfileModel {

    public Map<String, Double> calculateUserProfile(List<Long> userEventIds, Map<Long, Map<String, Double>> tfidfMap, String petBreed) {
        Map<String, Double> userProfile = new HashMap<>();

        TextProcessor textProcessor = new TextProcessor();

        String[] preprocessedPetBreed = textProcessor.preprocessTextForAttributes(petBreed);
        for(String p : preprocessedPetBreed){
            userProfile.put(p, 0.2);
        }

        for (Long eventId : userEventIds) {
            Map<String, Double> eventTFIDF = tfidfMap.get(eventId);
            if (eventTFIDF != null) {
                for (Map.Entry<String, Double> entry : eventTFIDF.entrySet()) {
                    String term = entry.getKey();
                    double tfidf = entry.getValue();
                    userProfile.put(term, userProfile.getOrDefault(term, 0.0) + tfidf);
                }
            }
        }

        // Normalize user profile vector
        normalizeUserProfile(userProfile);

        log.info("User profile vector after normalization {} ", userProfile);

        return userProfile;
    }

    private void normalizeUserProfile(Map<String, Double> userProfile) {
        double sum = userProfile.values().stream().mapToDouble(Double::doubleValue).sum();
        if (sum != 0.0) {
            for (Map.Entry<String, Double> entry : userProfile.entrySet()) {
                double normalizedValue = entry.getValue() / sum;
                entry.setValue(normalizedValue);
            }
        }
    }
}

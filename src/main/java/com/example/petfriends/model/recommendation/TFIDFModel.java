package com.example.petfriends.model.recommendation;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class TFIDFModel {
    public Map<Long, Map<String, Double>> calculateTFIDF(List<EventPreprocessed> events, String petBreed) {

        Map<Long, Map<String, Integer>> documentFrequencies = new HashMap<>();

        TextProcessor textProcessor = new TextProcessor();

        // Calculate document frequencies for all terms
        for (EventPreprocessed event : events) {
            List<String> terms = new ArrayList<>();
            String[] titleSplit = textProcessor.preprocessTextForAttributes(event.getTitle());
            terms.addAll(Arrays.asList(titleSplit));
            String[] descriptionSplit = textProcessor.preprocessTextForAttributes(event.getDescription());
            terms.addAll(Arrays.asList(descriptionSplit));
            terms.add(event.getCategory().toString());
            String[] locationSplit = textProcessor.preprocessTextForAttributes(event.getLocation());
            terms.addAll(Arrays.asList(locationSplit));
            terms.add(event.getCity());

            Map<String, Integer> termFrequencies = documentFrequencies.getOrDefault(event.getIdEvent(), new HashMap<>());

            for (String term : terms) {
                termFrequencies.put(term, termFrequencies.getOrDefault(term, 0) + 1);
            }

            documentFrequencies.put(event.getIdEvent(), termFrequencies);
        }

        Map<Long, Map<String, Double>> tfidfMap = new HashMap<>();

        String[] petBreedSplit = textProcessor.preprocessTextForAttributes(petBreed);

        Map<Long, Map<String, Double>> tfMap = new HashMap<>();

        // calculate TF score for each term in the document
        for(Map.Entry<Long, Map<String, Integer>> entry : documentFrequencies.entrySet()){
            Long eventId = entry.getKey();
            Map<String, Integer> termFrequencies = entry.getValue();

            for (Map.Entry<String, Integer> termEntry : termFrequencies.entrySet()) {
                String term = termEntry.getKey();
                Integer frequency = termEntry.getValue();
                Double tfScore = (double) frequency/termFrequencies.size();

                if(tfMap.containsKey(eventId)){
                    tfMap.get(eventId).put(term, tfScore);
                }
                else {
                    Map<String, Double> termTFScoreMap = new HashMap<>();
                    termTFScoreMap.put(term, tfScore);
                    tfMap.put(eventId, termTFScoreMap);
                }
            }
        }


        // Calculate TF-IDF values for each event
        for (EventPreprocessed event : events) {
            Map<String, Double> tfidfValues = new HashMap<>();
            List<String> terms = new ArrayList<>();

            String[] titleSplit = textProcessor.preprocessTextForAttributes(event.getTitle());
            terms.addAll(Arrays.asList(titleSplit));
            String[] descriptionSplit = textProcessor.preprocessTextForAttributes(event.getDescription());
            terms.addAll(Arrays.asList(descriptionSplit));
            terms.add(event.getCategory().toString());
            String[] locationSplit = textProcessor.preprocessTextForAttributes(event.getLocation());
            terms.addAll(Arrays.asList(locationSplit));
            terms.add(event.getCity());

            String[] termsArray = terms.toArray(new String[terms.size()]);

            for (String term : termsArray) {
                Map<String, Double> tfEntry = tfMap.get(event.getIdEvent());

                //calculate number of documents that contain a certain term
                Integer noDocumentsContainingTerm = 0;
                for (Map.Entry<Long, Map<String, Integer>> entry : documentFrequencies.entrySet())
                {
                    Map<String, Integer> termFrequencies = entry.getValue();
                    for (Map.Entry<String, Integer> termEntry : termFrequencies.entrySet()) {
                        String termDoc = termEntry.getKey();
                        if(termDoc.equalsIgnoreCase(term)){
                            noDocumentsContainingTerm++;
                            break;
                        }
                    }
                }

                double idf = calculateIDF(noDocumentsContainingTerm, events.size());
                double tfidf = tfEntry.get(term) * idf;

                for (String p: petBreedSplit) {
                    if(term.equalsIgnoreCase(p)) {
                        tfidf = 0.3;
                    }
                }

                log.info("tfidf for term {} is {} ", term, tfidf);

                tfidfValues.put(term, tfidf);
            }

            tfidfMap.put(event.getIdEvent(), tfidfValues);
        }

        return tfidfMap;
    }


    private double calculateIDF(int noDocumentsContainingTerm, int totalDocuments) {
        return Math.log((double) totalDocuments / (noDocumentsContainingTerm + 1));
    }
}

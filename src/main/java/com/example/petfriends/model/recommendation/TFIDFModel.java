package com.example.petfriends.model.recommendation;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class TFIDFModel {
    public Map<Long, Map<String, Double>> calculateTFIDF(List<EventPreprocessed> events, String petBreed) {
        Map<String, Integer> documentFrequencies = new HashMap<>();

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


            for (String term : terms) {
                documentFrequencies.put(term, documentFrequencies.getOrDefault(term, 0) + 1);
            }
        }
        log.info("document frequencies {}", documentFrequencies);



        Map<Long, Map<String, Double>> tfidfMap = new HashMap<>();

        String[] petBreedSplit = textProcessor.preprocessTextForAttributes(petBreed);

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

            int totalTerms = termsArray.length;

            for (String term : termsArray) {
                int termFrequency = countTermFrequency(termsArray, term);
                double tf = (double) termFrequency / totalTerms;
                double idf = calculateIDF(documentFrequencies.get(term), events.size());
                double tfidf = tf * idf;

                for (String p: petBreedSplit) {
                    if(term.equalsIgnoreCase(p)) {
                        log.info("AM INTRAT");
                        tfidf = 0.05;
                    }
                }

                log.info("tfidf for term {} is {} ", term, tfidf);

                tfidfValues.put(term, tfidf);
            }

            tfidfMap.put(event.getIdEvent(), tfidfValues);
        }

        log.info("Tfidf map {} ", tfidfMap);

        return tfidfMap;
    }

    private int countTermFrequency(String[] terms, String term) {
        int frequency = 0;
        for (String t : terms) {
            if (t.equals(term)) {
                frequency++;
            }
        }
        return frequency;
    }

    private double calculateIDF(int documentFrequency, int totalDocuments) {
        return Math.log((double) totalDocuments / (documentFrequency + 1));
    }
}

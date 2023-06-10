package com.example.petfriends.model.recommendation;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ro.RomanianAnalyzer;

import java.util.Arrays;
import java.util.Locale;

public class TextProcessor {

    private static final CharArraySet ENGLISH_STOP_WORDS = EnglishAnalyzer.getDefaultStopSet();
    private static final StopwordAnalyzerBase analyzer = new RomanianAnalyzer();
    private static final CharArraySet ROMANIAN_STOP_WORDS = analyzer.getStopwordSet();

    public static String preprocessText(String text) {
        // Remove punctuation
        text = text.replaceAll("[^a-zA-Z0-9\\s]", "");

        // Convert text to lowercase
        text = text.toLowerCase(Locale.ENGLISH);

        // Tokenize text into individual words
        String[] words = text.split("\\s+");

        // Remove stop words
        words = removeStopWords(words);

        return String.join(" ", words);
    }

    private static String[] removeStopWords(String[] words) {
        return Arrays.stream(words)
                .filter(word -> !ROMANIAN_STOP_WORDS.contains(word) && !ENGLISH_STOP_WORDS.contains(word))
                .toArray(String[]::new);

    }

    public static String[] preprocessTextForAttributes(String text){

        text = text.toLowerCase(Locale.ENGLISH);
        // Tokenize text into individual words
        String[] words = text.split("\\s+");

        return words;
    }
}

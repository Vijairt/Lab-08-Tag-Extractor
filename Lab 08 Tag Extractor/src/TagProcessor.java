import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class TagProcessor {

    public static Set<String> loadStopWords(File file) throws IOException {
        Set<String> stopWords = new TreeSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String word;
            while ((word = br.readLine()) != null) {
                stopWords.add(word.trim().toLowerCase());
            }
        }
        return stopWords;
    }

    public static Map<String, Integer> extractTags(File file, Set<String> stopWords) throws IOException {
        Map<String, Integer> tagMap = new TreeMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.replaceAll("[^a-zA-Z]", " ").toLowerCase().split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty() && !stopWords.contains(word)) {
                        tagMap.put(word, tagMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }
        return tagMap;
    }

    public static void saveTagsToFile(Map<String, Integer> tagMap, File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Integer> entry : tagMap.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        }
    }
}


package com.company.data.util;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import java.util.*;


public class FilterSimilarNames {

    private static final LevenshteinDistance levenshtein = new LevenshteinDistance();

    public static double checkString(String s1, String s2) {
        String a = s1.toLowerCase().replaceAll("[^a-z0-9 ]", "").trim();
        String b = s2.toLowerCase().replaceAll("[^a-z0-9 ]", "").trim();

        Set<String> tokensA = new HashSet<>(Arrays.asList(a.split("\\s+")));
        Set<String> tokensB = new HashSet<>(Arrays.asList(b.split("\\s+")));

        Set<String> intersection = new HashSet<>(tokensA);
        intersection.retainAll(tokensB);

        double tokenOverlap = (double) intersection.size() /
                ((tokensA.size() + tokensB.size()) / 2.0);

        int distance = levenshtein.apply(a, b);
        int maxLen = Math.max(a.length(), b.length());
        double levSimilarity = 1.0 - ((double) distance / maxLen);

        return (tokenOverlap + levSimilarity) / 2.0;
    }

    public static List<String> filterAllNames(List<String> names, double threshold) {
        if (names.isEmpty()) return Collections.emptyList();

        String siteName = names.get(0);
        List<String> result = new ArrayList<>();
        result.add(siteName);

        for (int i = 1; i < names.size(); i++) {
            double score = checkString(siteName, names.get(i));
            if (score >= threshold) {
                result.add(names.get(i));
            }
        }

        return result;
    }
}

package com.epms.service;
import com.epms.dao.Reportable;
import java.util.*;
import java.util.stream.Collectors;

/** Calculates report metrics using collections and stream operations. */
public class ReportService implements Reportable {
    /** Calculates department averages from rating rows. */
    public Map<String, Double> calculateDepartmentAverages(Map<String, List<Integer>> ratings) {
        return ratings.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                e -> e.getValue().stream().mapToInt(Integer::intValue).average().orElse(0)));
    }
    @Override public Map<String, Double> departmentAverages() { return new LinkedHashMap<>(); }
    /** Returns the maximum comparable item using a generic bounded type. */
    public static <T extends Comparable<T>> T findMax(List<T> values) {
        return values.stream().max(Comparator.naturalOrder()).orElse(null);
    }
}

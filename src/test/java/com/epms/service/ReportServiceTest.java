package com.epms.service;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
/** Verifies report aggregation. */
class ReportServiceTest {
    @Test void calculatesDepartmentAverage() {
        Map<String, List<Integer>> input = Map.of("Engineering", List.of(4, 5));
        assertEquals(4.5, new ReportService().calculateDepartmentAverages(input).get("Engineering"));
    }
}

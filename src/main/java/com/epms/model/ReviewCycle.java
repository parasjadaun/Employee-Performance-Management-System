package com.epms.model;

import java.time.LocalDate;

/** Configurable review period. */
public class ReviewCycle {
    private int id; private String name; private LocalDate startDate; private LocalDate endDate; private String status;
    public int getId() { return id; } public void setId(int v) { id = v; }
    public String getName() { return name; } public void setName(String v) { name = v; }
    public LocalDate getStartDate() { return startDate; } public void setStartDate(LocalDate v) { startDate = v; }
    public LocalDate getEndDate() { return endDate; } public void setEndDate(LocalDate v) { endDate = v; }
    public String getStatus() { return status; } public void setStatus(String v) { status = v; }
}

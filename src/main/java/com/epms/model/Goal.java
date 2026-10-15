package com.epms.model;

import java.time.LocalDate;

/** A weighted, measurable employee goal. */
public class Goal {
    private int id;
    private int employeeId;
    private int managerId;
    private String title;
    private String description;
    private double weight;
    private LocalDate dueDate;
    private int progress;
    public int getId() { return id; } public void setId(int v) { id = v; }
    public int getEmployeeId() { return employeeId; } public void setEmployeeId(int v) { employeeId = v; }
    public int getManagerId() { return managerId; } public void setManagerId(int v) { managerId = v; }
    public String getTitle() { return title; } public void setTitle(String v) { title = v; }
    public String getDescription() { return description; } public void setDescription(String v) { description = v; }
    public double getWeight() { return weight; } public void setWeight(double v) { weight = v; }
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate v) { dueDate = v; }
    public int getProgress() { return progress; } public void setProgress(int v) { progress = v; }
}

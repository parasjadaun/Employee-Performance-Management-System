package com.epms.model;

/** Manager responsible for team goals and reviews. */
public class Manager extends User {
    public Manager(int id, String name, String email, String passwordHash, Integer departmentId) {
        super(id, name, email, passwordHash, "MANAGER", departmentId);
    }
    @Override public String getDashboardPath() { return "/WEB-INF/views/manager/dashboard.jsp"; }
    @Override public double calculateBonus(double averageRating) { return averageRating * 750.0; }
    @Override public String getRoleLabel() { return "Manager"; }
}

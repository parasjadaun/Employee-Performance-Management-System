package com.epms.model;

/** Employee who tracks goals and submits self-assessments. */
public class Employee extends User {
    public Employee(int id, String name, String email, String passwordHash, Integer departmentId) {
        super(id, name, email, passwordHash, "EMPLOYEE", departmentId);
    }
    @Override public String getDashboardPath() { return "/WEB-INF/views/employee/dashboard.jsp"; }
    @Override public double calculateBonus(double averageRating) { return averageRating * 500.0; }
    @Override public String getRoleLabel() { return "Employee"; }
}

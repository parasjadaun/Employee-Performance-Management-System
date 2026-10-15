package com.epms.model;

/** Administrator with organization-wide permissions. */
public class Admin extends User {
    public Admin(int id, String name, String email, String passwordHash, Integer departmentId) {
        super(id, name, email, passwordHash, "ADMIN", departmentId);
    }
    @Override public String getDashboardPath() { return "/WEB-INF/views/admin/dashboard.jsp"; }
    @Override public double calculateBonus(double averageRating) { return averageRating * 100.0; }
    @Override public String getRoleLabel() { return "Administrator"; }
}

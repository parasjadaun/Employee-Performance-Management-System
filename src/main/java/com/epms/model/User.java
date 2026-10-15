package com.epms.model;

import java.util.Objects;

/**
 * Common authenticated user state.
 */
public abstract class User {
    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private String role;
    private Integer departmentId;

    protected User() {
    }

    protected User(int id, String name, String email, String passwordHash, String role, Integer departmentId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.departmentId = departmentId;
    }

    /** @return dashboard path selected polymorphically */
    public abstract String getDashboardPath();

    /** Calculates a role-specific bonus. */
    public abstract double calculateBonus(double averageRating);

    /** @return display label for the role */
    public abstract String getRoleLabel();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public Integer getDepartmentId() { return departmentId; }
    public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof User user)) return false;
        return id == user.id && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() { return Objects.hash(id, email); }

    @Override
    public String toString() { return "User{id=" + id + ", name='" + name + "', role='" + role + "'}"; }
}

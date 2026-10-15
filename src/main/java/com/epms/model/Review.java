package com.epms.model;

/** A manager or employee rating for a review cycle. */
public class Review {
    private int id;
    private int cycleId;
    private int employeeId;
    private int reviewerId;
    private int rating;
    private String comments;
    private String strengths;
    private String improvements;
    private String type;

    public Review() { }
    public Review(int cycleId, int employeeId, int reviewerId, int rating, String comments,
                  String strengths, String improvements, String type) {
        this.cycleId = cycleId; this.employeeId = employeeId; this.reviewerId = reviewerId;
        this.rating = rating; this.comments = comments; this.strengths = strengths;
        this.improvements = improvements; this.type = type;
    }
    public int getId() { return id; } public void setId(int v) { id = v; }
    public int getCycleId() { return cycleId; } public void setCycleId(int v) { cycleId = v; }
    public int getEmployeeId() { return employeeId; } public void setEmployeeId(int v) { employeeId = v; }
    public int getReviewerId() { return reviewerId; } public void setReviewerId(int v) { reviewerId = v; }
    public int getRating() { return rating; } public void setRating(int v) { rating = v; }
    public String getComments() { return comments; } public void setComments(String v) { comments = v; }
    public String getStrengths() { return strengths; } public void setStrengths(String v) { strengths = v; }
    public String getImprovements() { return improvements; } public void setImprovements(String v) { improvements = v; }
    public String getType() { return type; } public void setType(String v) { type = v; }
}

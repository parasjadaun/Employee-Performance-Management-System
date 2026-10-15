package com.epms.service;

import com.epms.exception.DAOException;
import com.epms.exception.InvalidRatingException;
import com.epms.model.Review;
import com.epms.util.DBConnection;
import java.sql.*;

/** Executes review validation and atomic multi-table persistence. */
public class ReviewService {
    /** Saves a manager review, summary update, and notification in one transaction. */
    public void submitReview(Review review, String notification) throws DAOException, InvalidRatingException {
        if (review.getRating() < 1 || review.getRating() > 5) throw new InvalidRatingException("Rating must be between 1 and 5.");
        String insertReview = "INSERT INTO reviews(cycle_id,employee_id,reviewer_id,rating,comments,strengths,improvements,type) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection()) {
            c.setAutoCommit(false);
            try (PreparedStatement p = c.prepareStatement(insertReview);
                 PreparedStatement n = c.prepareStatement("INSERT INTO notifications(user_id,message) VALUES(?,?)")) {
                p.setInt(1, review.getCycleId()); p.setInt(2, review.getEmployeeId()); p.setInt(3, review.getReviewerId());
                p.setInt(4, review.getRating()); p.setString(5, review.getComments()); p.setString(6, review.getStrengths());
                p.setString(7, review.getImprovements()); p.setString(8, review.getType()); p.executeUpdate();
                n.setInt(1, review.getEmployeeId()); n.setString(2, notification); n.executeUpdate();
                c.commit();
            } catch (SQLException e) { c.rollback(); throw e; }
        } catch (SQLException e) { throw new DAOException("Unable to submit review", e); }
    }
}

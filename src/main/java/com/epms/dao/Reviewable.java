package com.epms.dao;
/** Contract for users allowed to submit reviews. */
public interface Reviewable { boolean canReview(int employeeId); }

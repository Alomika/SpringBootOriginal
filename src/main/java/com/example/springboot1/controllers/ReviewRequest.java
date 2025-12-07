package com.example.springboot1.controllers;

import java.time.LocalDate;

public class ReviewRequest {
    private int commentOwnerId;
    private int feedbackUserId;
    private int rating;
    private String reviewText;

    public int getCommentOwnerId() {
        return commentOwnerId;
    }

    public void setCommentOwnerId(int commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
    }

    public int getFeedbackUserId() {
        return feedbackUserId;
    }

    public void setFeedbackUserId(int feedbackUserId) {
        this.feedbackUserId = feedbackUserId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
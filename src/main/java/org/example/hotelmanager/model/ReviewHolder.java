package org.example.hotelmanager.model;

public final class ReviewHolder {
    private Review review;
    private final static ReviewHolder INSTANCE = new ReviewHolder();

    private ReviewHolder(){}
    public static ReviewHolder getInstance() {
        return INSTANCE;
    }
    public void setReview(Review review) {
        this.review = review;
    }
    public Review getReview() {
        return this.review;
    }
}
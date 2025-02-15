package com.lancaster.app.model;

public class Review {
    private int rating;
    private int reviewId;
    private String author;
    private String title;
    private String description;


    Review(int rating, int reviewId, String  author, String title, String description) {
        this.reviewId = reviewId;
        this.rating = rating;
        this.author = author;
        this.title = title;
        this.description = description;
    }


    // Getters and Setters.

    public int getRating() {
        return rating;
    }

    public int getReviewId() {
        return reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
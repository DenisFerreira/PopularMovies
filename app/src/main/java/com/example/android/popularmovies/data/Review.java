package com.example.android.popularmovies.data;

/**
 * Created by lsitec205.ferreira on 24/11/17.
 */

public class Review {
    private String reviewId;
    private String author;
    private String content;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

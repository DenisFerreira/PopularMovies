package com.example.android.popularmovies.data;

/**
 * Created by lsitec205.ferreira on 24/11/17.
 */

public class Review {
    private String review_id;
    private String author;
    private String content;

    public String getReview_id() {
        return review_id;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
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

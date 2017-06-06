package com.onedudedesign.popularmoviess2.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by clucier on 5/19/17.
 * POJO for the movie reviews object to be used by the adapter
 */

public class MovieReviews {

    //setup the data variables
    @SerializedName("id")
    private String reviewID;

    @SerializedName("author")
    private String reviewAuthor;

    @SerializedName("content")
    private String reviewContent;

    //Getters and Setters

    public String getReviewID() {
        return reviewID;
    }

    public void setReviewID(String reviewID) {
        this.reviewID = reviewID;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }



    public static class MovieReviewsResult {
        private List<MovieReviews> results;

        public List<MovieReviews> getResults() {
            return results;
        }
    }
}

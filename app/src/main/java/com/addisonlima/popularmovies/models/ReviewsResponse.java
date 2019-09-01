package com.addisonlima.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class ReviewsResponse {

    @SerializedName("results")
    private Review[] mReviews;

    public Review[] getReviews() {
        return mReviews;
    }
}

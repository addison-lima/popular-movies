package com.addisonlima.popularmovies.model;

import com.google.gson.annotations.SerializedName;

public class MoviesResponse {

    @SerializedName("results")
    private Movie[] mMovies;

    public Movie[] getMovies() {
        return mMovies;
    }
}

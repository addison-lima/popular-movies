package com.addisonlima.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class MoviesResponse {

    @SerializedName("results")
    private Movie[] mMovies;

    public Movie[] getMovies() {
        return mMovies;
    }

    public void setMovies(Movie[] movies) {
        mMovies = movies;
    }
}

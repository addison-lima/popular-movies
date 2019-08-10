package com.addisonlima.popularmovies;

import retrofit.Callback;
import retrofit.http.GET;

public interface ITMDbApiService {

    @GET("/movie/popular")
    void getPopularMovies(Callback<Movie.MovieResult> cb);
}

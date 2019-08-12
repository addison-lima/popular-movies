package com.addisonlima.popularmovies.repository;

import com.addisonlima.popularmovies.models.MoviesResponse;

import retrofit.Callback;
import retrofit.http.GET;

public interface TMDbApi {

    @GET("/movie/popular")
    void getPopularMovies(Callback<MoviesResponse> cb);

    @GET("/movie/top_rated")
    void getTopRatedMovies(Callback<MoviesResponse> cb);
}

package com.addisonlima.popularmovies.repository;

import com.addisonlima.popularmovies.models.MoviesResponse;
import com.addisonlima.popularmovies.models.ReviewsResponse;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface TMDbApi {

    @GET("/movie/popular")
    void getPopularMovies(Callback<MoviesResponse> cb);

    @GET("/movie/top_rated")
    void getTopRatedMovies(Callback<MoviesResponse> cb);

    @GET("/movie/{id}/reviews")
    void getReviews(@Path("id") String id, Callback<ReviewsResponse> cb);
}

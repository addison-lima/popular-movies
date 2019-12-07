package com.addisonlima.popularmovies.repository;

import com.addisonlima.popularmovies.model.Game;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TMDbApi {

    @POST("games")
    Call<List<Game>> getGames(@Body String query);
}

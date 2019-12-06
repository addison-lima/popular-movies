package com.addisonlima.popularmovies.repository;

import android.arch.lifecycle.LiveData;

import com.addisonlima.popularmovies.model.Game;
import com.github.leonardoxh.livedatacalladapter.Resource;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TMDbApi {

    @POST("/games")
    LiveData<Resource<List<Game>>> getGames(@Body String query);
}

package com.addisonlima.popularmovies;

import android.util.Log;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class Repository {

    private static final String TMDB_END_POINT = "http://api.themoviedb.org/3";
    private static final String TMDB_API_KEY_PARAM_NAME = "api_key";
    private static final String TMDB_API_KEY_VALUE = BuildConfig.TMDB_API_KEY;

    public static void getMovies() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(TMDB_END_POINT)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam(TMDB_API_KEY_PARAM_NAME, TMDB_API_KEY_VALUE);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ITMDbApiService service = restAdapter.create(ITMDbApiService.class);
        service.getPopularMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult moviesResult, Response response) {
                Log.d("ADD_TEST", "success!!!");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ADD_TEST", "failure: " + error.getKind().name());
            }
        });
    }
}

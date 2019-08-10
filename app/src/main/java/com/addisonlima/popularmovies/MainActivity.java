package com.addisonlima.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPopularMovies("");
    }

    private void getPopularMovies(String top_rated) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", "8f5f6df0585d0d2b95c786ab35858e78");
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        ITMDbApiService service = restAdapter.create(ITMDbApiService.class);
        service.getPopularMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                Log.d("ADD_TEST", "success!!!");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("ADD_TEST", "failure: " + error.toString());
            }
        });
    }}

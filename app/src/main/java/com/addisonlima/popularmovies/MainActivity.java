package com.addisonlima.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.addisonlima.popularmovies.model.MoviesResponse;
import com.addisonlima.popularmovies.model.RequestStatus;
import com.addisonlima.popularmovies.viewmodel.MoviesViewModel;

public class MainActivity extends AppCompatActivity {

    MoviesViewModel mMoviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        mMoviesViewModel.getRequestStatus().observe(this, getRequestStatusObserver());
        mMoviesViewModel.getMoviesResponse().observe(this, getMoviesResponseObserver());
    }

    private Observer<RequestStatus> getRequestStatusObserver() {
        return new Observer<RequestStatus>() {
            @Override
            public void onChanged(@Nullable RequestStatus requestStatus) {
                if (requestStatus != null) {
                    Log.d("ADD_TEST", "SortType: " + requestStatus.getSortType());
                    Log.d("ADD_TEST", "RequestState: " + requestStatus.getRequestState());
                }
            }
        };
    }

    private Observer<MoviesResponse> getMoviesResponseObserver() {
        return new Observer<MoviesResponse>() {
            @Override
            public void onChanged(@Nullable MoviesResponse moviesResponse) {
                if (moviesResponse != null) {
                    Log.d("ADD_TEST", "movies list size: " + moviesResponse.getMovies().get(0).getOriginalTitle());
                }
            }
        };
    }
}

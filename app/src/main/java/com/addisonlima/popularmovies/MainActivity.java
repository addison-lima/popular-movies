package com.addisonlima.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.addisonlima.popularmovies.model.Movie;
import com.addisonlima.popularmovies.model.MoviesResponse;
import com.addisonlima.popularmovies.model.RequestStatus;
import com.addisonlima.popularmovies.view.MoviesAdapter;
import com.addisonlima.popularmovies.viewmodel.MoviesViewModel;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private RecyclerView mRvMovies;
    private MoviesAdapter mMoviesAdapter;

    private MoviesViewModel mMoviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int orientation = getResources().getConfiguration().orientation;
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 5 : 3;

        mMoviesAdapter = new MoviesAdapter(this, this);

        mRvMovies = findViewById(R.id.rvMovies);
        mRvMovies.setLayoutManager(new GridLayoutManager(this, spanCount));
        mRvMovies.setAdapter(mMoviesAdapter);

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
                    mMoviesAdapter.setMoviesData(moviesResponse.getMovies());
                }
            }
        };
    }

    @Override
    public void onClick(Movie movie) {
        Toast.makeText(this, movie.getOriginalTitle(), Toast.LENGTH_LONG).show();
    }
}

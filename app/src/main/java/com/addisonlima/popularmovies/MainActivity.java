package com.addisonlima.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.addisonlima.popularmovies.models.Movie;
import com.addisonlima.popularmovies.models.MoviesResponse;
import com.addisonlima.popularmovies.models.RequestStatus;
import com.addisonlima.popularmovies.models.RequestStatus.RequestState;
import com.addisonlima.popularmovies.adapters.MoviesAdapter;
import com.addisonlima.popularmovies.viewmodels.MoviesViewModel;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private MoviesAdapter mMoviesAdapter;

    private MoviesViewModel mMoviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int orientation = getResources().getConfiguration().orientation;
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 5 : 3;

        mMoviesAdapter = new MoviesAdapter(this, this);

        RecyclerView rvMovies = findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, spanCount));
        rvMovies.setAdapter(mMoviesAdapter);

        mMoviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        mMoviesViewModel.getRequestStatus().observe(this, getRequestStatusObserver());
        mMoviesViewModel.getMoviesResponse().observe(this, getMoviesResponseObserver());
    }

    private Observer<RequestStatus> getRequestStatusObserver() {
        return new Observer<RequestStatus>() {
            @Override
            public void onChanged(@Nullable RequestStatus requestStatus) {
                if (requestStatus != null) {
                    RequestState requestState = requestStatus.getRequestState();

                    RecyclerView rvMovies = findViewById(R.id.rv_movies);
                    rvMovies.setVisibility(
                            (requestState.equals(RequestState.SUCCESS))
                                    ? View.VISIBLE : View.INVISIBLE);

                    TextView tvFailureMessage = findViewById(R.id.tv_failure_message);
                    tvFailureMessage.setVisibility(
                            (requestState.equals(RequestState.FAILURE))
                                    ? View.VISIBLE : View.INVISIBLE);

                    ProgressBar pbLoadingIndicator = findViewById(R.id.pb_loading_indicator);
                    pbLoadingIndicator.setVisibility(
                            (requestState.equals(RequestState.LOADING))
                                    ? View.VISIBLE : View.INVISIBLE);
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

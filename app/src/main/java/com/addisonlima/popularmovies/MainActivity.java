package com.addisonlima.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.addisonlima.popularmovies.database.FavoriteEntry;
import com.addisonlima.popularmovies.models.Movie;
import com.addisonlima.popularmovies.models.MoviesResponse;
import com.addisonlima.popularmovies.models.RequestStatus;
import com.addisonlima.popularmovies.models.RequestStatus.RequestState;
import com.addisonlima.popularmovies.models.RequestStatus.SortType;
import com.addisonlima.popularmovies.adapters.MoviesAdapter;
import com.addisonlima.popularmovies.viewmodels.MoviesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.MoviesAdapterOnClickHandler {

    private MoviesAdapter mMoviesAdapter;
    private MoviesAdapter mFavoriteMoviesAdapter;

    private MoviesViewModel mMoviesViewModel;

    private RequestStatus mRequestStatus = new RequestStatus(SortType.POPULAR, RequestState.LOADING);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int orientation = getResources().getConfiguration().orientation;
        int spanCount = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 5 : 3;

        mMoviesAdapter = new MoviesAdapter(this, this);
        mFavoriteMoviesAdapter = new MoviesAdapter(this, this);

        RecyclerView rvMovies = findViewById(R.id.rv_movies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, spanCount));
        rvMovies.setAdapter(mMoviesAdapter);

        RecyclerView rvFavoriteMovies = findViewById(R.id.rv_favorite_movies);
        rvFavoriteMovies.setLayoutManager(new GridLayoutManager(this, spanCount));
        rvFavoriteMovies.setAdapter(mFavoriteMoviesAdapter);

        mMoviesViewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        mMoviesViewModel.getRequestStatus().observe(this, getRequestStatusObserver());
        mMoviesViewModel.getMoviesResponse().observe(this, getMoviesResponseObserver());
        mMoviesViewModel.getFavoriteMoviesResponse().observe(this, getFavoriteMoviesResponseObserver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_favorite:
                mMoviesViewModel.sortMoviesBy(SortType.FAVORITE);
                return true;
            case R.id.action_popular:
                mMoviesViewModel.sortMoviesBy(SortType.POPULAR);
                return true;
            case R.id.action_top_rated:
                mMoviesViewModel.sortMoviesBy(SortType.TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE, movie);
        startActivity(intent);
    }

    private Observer<RequestStatus> getRequestStatusObserver() {
        return new Observer<RequestStatus>() {
            @Override
            public void onChanged(@Nullable RequestStatus requestStatus) {
                if (requestStatus != null) {
                    mRequestStatus = requestStatus;
                    updateUi();
                    updateActionBar();
                }
            }
        };
    }

    private void updateUi() {
        RecyclerView rvMovies = MainActivity.this.findViewById(R.id.rv_movies);
        RecyclerView rvFavoriteMovies = MainActivity.this.findViewById(R.id.rv_favorite_movies);
        TextView tvEmptyMessage = MainActivity.this.findViewById(R.id.tv_empty_message);
        TextView tvFailureMessage = MainActivity.this.findViewById(R.id.tv_failure_message);
        ProgressBar pbLoadingIndicator = MainActivity.this.findViewById(R.id.pb_loading_indicator);

        switch (mRequestStatus.getRequestState()) {
            case SUCCESS:
                if (mRequestStatus.getSortType().equals(SortType.FAVORITE)) {
                    rvMovies.setVisibility(View.INVISIBLE);
                    if (mFavoriteMoviesAdapter.getItemCount() > 0) {
                        rvFavoriteMovies.setVisibility(View.VISIBLE);
                        tvEmptyMessage.setVisibility(View.INVISIBLE);
                    } else {
                        rvFavoriteMovies.setVisibility(View.INVISIBLE);
                        tvEmptyMessage.setVisibility(View.VISIBLE);
                    }
                } else {
                    rvMovies.setVisibility(View.VISIBLE);
                    rvFavoriteMovies.setVisibility(View.INVISIBLE);
                    tvEmptyMessage.setVisibility(View.INVISIBLE);
                }
                tvFailureMessage.setVisibility(View.INVISIBLE);
                pbLoadingIndicator.setVisibility(View.INVISIBLE);
                break;
            case FAILURE:
                rvMovies.setVisibility(View.INVISIBLE);
                rvFavoriteMovies.setVisibility(View.INVISIBLE);
                tvEmptyMessage.setVisibility(View.INVISIBLE);
                tvFailureMessage.setVisibility(View.VISIBLE);
                pbLoadingIndicator.setVisibility(View.INVISIBLE);
                break;
            case LOADING:
                rvMovies.setVisibility(View.INVISIBLE);
                rvFavoriteMovies.setVisibility(View.INVISIBLE);
                tvEmptyMessage.setVisibility(View.INVISIBLE);
                tvFailureMessage.setVisibility(View.INVISIBLE);
                pbLoadingIndicator.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void updateActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            switch (mRequestStatus.getSortType()) {
                case FAVORITE:
                    actionBar.setTitle(R.string.action_favorite);
                    break;
                case POPULAR:
                    actionBar.setTitle(R.string.action_popular);
                    break;
                case TOP_RATED:
                    actionBar.setTitle(R.string.action_top_rated);
                    break;
                default:
                    break;
            }
        }
    }

    private Observer<MoviesResponse> getMoviesResponseObserver() {
        return new Observer<MoviesResponse>() {
            @Override
            public void onChanged(@Nullable MoviesResponse moviesResponse) {
                if (moviesResponse != null) {
                    mMoviesAdapter.setMoviesData(moviesResponse.getMovies());
                    updateUi();
                }
            }
        };
    }

    private Observer<List<FavoriteEntry>> getFavoriteMoviesResponseObserver() {
        return new Observer<List<FavoriteEntry>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteEntry> favoriteEntryList) {
                if (favoriteEntryList != null) {
                    mFavoriteMoviesAdapter.setMoviesData(convertToMovies(favoriteEntryList));
                    updateUi();
                }
            }
        };
    }

    private Movie[] convertToMovies(List<FavoriteEntry> favoriteEntryList) {
        Movie[] movies = new Movie[favoriteEntryList.size()];

        for(int i = 0; i < favoriteEntryList.size(); i++) {
            movies[i] = convertToMovie(favoriteEntryList.get(i));
        }

        return movies;
    }

    private Movie convertToMovie(FavoriteEntry favoriteEntry) {
        return new Movie(favoriteEntry.getId(), favoriteEntry.getTitle(),
                favoriteEntry.getOriginalTitle(), favoriteEntry.getPosterPath(),
                favoriteEntry.getOverview(), favoriteEntry.getVoteAverage(),
                favoriteEntry.getReleaseDate());
    }
}

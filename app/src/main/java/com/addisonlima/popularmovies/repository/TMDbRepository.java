package com.addisonlima.popularmovies.repository;

import android.arch.lifecycle.MutableLiveData;

import com.addisonlima.popularmovies.BuildConfig;
import com.addisonlima.popularmovies.models.MoviesResponse;
import com.addisonlima.popularmovies.models.RequestStatus;
import com.addisonlima.popularmovies.models.RequestStatus.RequestState;
import com.addisonlima.popularmovies.models.RequestStatus.SortType;
import com.addisonlima.popularmovies.models.ReviewsResponse;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TMDbRepository {

    private static final String TMDB_END_POINT = "http://api.themoviedb.org/3";
    private static final String TMDB_API_KEY_PARAM_NAME = "api_key";
    private static final String TMDB_API_KEY_VALUE = BuildConfig.TMDB_API_KEY;

    private final MutableLiveData<MoviesResponse> mMoviesResponse = new MutableLiveData<>();
    private final MutableLiveData<RequestStatus> mRequestStatus = new MutableLiveData<>();
    private final TMDbApi mService;

    public TMDbRepository() {
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

        mService = restAdapter.create(TMDbApi.class);

        sortMoviesBy(SortType.POPULAR);
    }

    public MutableLiveData<MoviesResponse> getMoviesResponse() {
        return mMoviesResponse;
    }

    public MutableLiveData<RequestStatus> getRequestStatus() {
        return mRequestStatus;
    }

    public void sortMoviesBy(SortType sortType) {
        switch (sortType) {
            case POPULAR:
                mService.getPopularMovies(getMoviesResponseCallback(SortType.POPULAR));
                break;
            case TOP_RATED:
                mService.getTopRatedMovies(getMoviesResponseCallback(SortType.TOP_RATED));
                break;
            default:
                break;
        }
    }

    public void getReviewsById(String id) {
        mService.getReviews(id, getReviewsResponseCallback());
    }

    private Callback<MoviesResponse> getMoviesResponseCallback(final SortType sortType) {
        mRequestStatus.setValue(new RequestStatus(sortType, RequestState.LOADING));
        return new Callback<MoviesResponse>() {
            @Override
            public void success(MoviesResponse moviesResponse, Response response) {
                mMoviesResponse.setValue(moviesResponse);
                mRequestStatus.setValue(new RequestStatus(sortType, RequestState.SUCCESS));
            }

            @Override
            public void failure(RetrofitError error) {
                mRequestStatus.setValue(new RequestStatus(sortType, RequestState.FAILURE));
            }
        };
    }

    private Callback<ReviewsResponse> getReviewsResponseCallback() {
        return new Callback<ReviewsResponse>() {
            @Override
            public void success(ReviewsResponse reviewsResponse, Response response) {
                //TODO implement success reviews response
            }

            @Override
            public void failure(RetrofitError error) {
                //TODO implement failure reviews response
            }
        };
    }
}

package com.addisonlima.popularmovies.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.addisonlima.popularmovies.BuildConfig;
import com.addisonlima.popularmovies.database.FavoriteDatabase;
import com.addisonlima.popularmovies.database.FavoriteEntry;
import com.addisonlima.popularmovies.models.Movie;
import com.addisonlima.popularmovies.models.MoviesResponse;
import com.addisonlima.popularmovies.models.RequestStatus;
import com.addisonlima.popularmovies.models.RequestStatus.RequestState;
import com.addisonlima.popularmovies.models.RequestStatus.SortType;
import com.addisonlima.popularmovies.models.ReviewsResponse;
import com.addisonlima.popularmovies.models.VideosResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TMDbRepository {

    private static final String TMDB_END_POINT = "http://api.themoviedb.org/3";
    private static final String TMDB_API_KEY_PARAM_NAME = "api_key";
    private static final String TMDB_API_KEY_VALUE = BuildConfig.TMDB_API_KEY;

    private static final Object LOCK = new Object();
    private static TMDbRepository sInstance;

    private final MutableLiveData<MoviesResponse> mMoviesResponse = new MutableLiveData<>();
    private final MutableLiveData<RequestStatus> mRequestStatus = new MutableLiveData<>();

    private final MutableLiveData<ReviewsResponse> mReviewsResponse = new MutableLiveData<>();

    private final TMDbApi mService;
    private final FavoriteDatabase mFavoriteDatabase;

    private TMDbRepository(Context context) {
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

        mFavoriteDatabase = FavoriteDatabase.getInstance(context);

        sortMoviesBy(SortType.POPULAR);
    }

    public static TMDbRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new TMDbRepository(context);
            }
        }
        return sInstance;
    }

    public LiveData<List<FavoriteEntry>> getFavoriteMoviesResponse() {
        return mFavoriteDatabase.favoriteDao().loadFavoriteMovies();
    }

    public MutableLiveData<MoviesResponse> getMoviesResponse() {
        return mMoviesResponse;
    }

    public MutableLiveData<RequestStatus> getRequestStatus() {
        return mRequestStatus;
    }

    public MutableLiveData<ReviewsResponse> getReviewsResponse() {
        return mReviewsResponse;
    }

    public void sortMoviesBy(SortType sortType) {
        switch (sortType) {
            case FAVORITE:
                mRequestStatus.setValue(new RequestStatus(SortType.FAVORITE, RequestState.SUCCESS));
                break;
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

    public void markAsFavorite(Movie movie) {
        final FavoriteEntry favoriteEntry = convertToFavoriteEntry(movie);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mFavoriteDatabase.favoriteDao().insertFavorite(favoriteEntry);
                return null;
            }
        }.execute();
    }

    public void unmarkAsFavorite(Movie movie) {
        final FavoriteEntry favoriteEntry = convertToFavoriteEntry(movie);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mFavoriteDatabase.favoriteDao().deleteFavorite(favoriteEntry);
                return null;
            }
        }.execute();
    }

    public void getReviewsById(String id) {
        mService.getReviews(id, getReviewsResponseCallback());
    }

    public void getVideosById(String id) {
        mService.getVideos(id, getVideosResponseCallback());
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
                mReviewsResponse.setValue(reviewsResponse);
            }

            @Override
            public void failure(RetrofitError error) {
                // Do Nothing
            }
        };
    }

    private Callback<VideosResponse> getVideosResponseCallback() {
        return new Callback<VideosResponse>() {
            @Override
            public void success(VideosResponse videosResponse, Response response) {
                //TODO implement success videos response
            }

            @Override
            public void failure(RetrofitError error) {
                //TODO implement failure videos response
            }
        };
    }

    private FavoriteEntry convertToFavoriteEntry(Movie movie) {
        return new FavoriteEntry(movie.getId(), movie.getTitle(), movie.getOriginalTitle(),
                movie.getPosterPath(), movie.getOverview(), movie.getVoteAverage(),
                movie.getReleaseDate());
    }
}

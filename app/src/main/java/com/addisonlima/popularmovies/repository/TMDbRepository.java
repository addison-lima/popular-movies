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
    private final TMDbApi mService;

    private final FavoriteDatabase mFavoriteDatabase;

    private TMDbRepository(Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(TMDB_END_POINT)
                .setRequestInterceptor(request -> request
                        .addEncodedQueryParam(TMDB_API_KEY_PARAM_NAME, TMDB_API_KEY_VALUE))
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

    public void sortMoviesBy(SortType sortType) {
        switch (sortType) {
            case FAVORITE:
                mRequestStatus.setValue(new RequestStatus(SortType.FAVORITE, RequestState.EMPTY));
//                getFavoriteMovies();
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

    public void getReviewsById(String id) {
        mService.getReviews(id, getReviewsResponseCallback());
    }

    public void getVideosById(String id) {
        mService.getVideos(id, getVideosResponseCallback());
    }

    private void getFavoriteMovies() {
        LiveData<List<FavoriteEntry>> favoriteEntryLiveData = mFavoriteDatabase.favoriteDao()
                .loadFavoriteMovies();
        if (favoriteEntryLiveData != null && favoriteEntryLiveData.getValue() != null) {
            onFavoriteEntryChanged(favoriteEntryLiveData.getValue());
        }
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

//  https://android.jlelse.eu/5-steps-to-implement-room-persistence-library-in-android-47b10cd47b24

    private void onFavoriteEntryChanged(List<FavoriteEntry> favoriteEntryList) {

        if (favoriteEntryList == null || favoriteEntryList.isEmpty()) {
            mRequestStatus.setValue(new RequestStatus(SortType.FAVORITE, RequestState.EMPTY));
        } else {
            mMoviesResponse.setValue(convertToMoviesResponse(favoriteEntryList));
            mRequestStatus.setValue(new RequestStatus(SortType.FAVORITE, RequestState.SUCCESS));
        }
    }

    private FavoriteEntry convertToFavoriteEntry(Movie movie) {
        return new FavoriteEntry(movie.getId(), movie.getTitle(), movie.getOriginalTitle(),
                movie.getPosterPath(), movie.getOverview(), movie.getVoteAverage(),
                movie.getReleaseDate());
    }

    private Movie convertToMovie(FavoriteEntry favoriteEntry) {
        return new Movie(favoriteEntry.getId(), favoriteEntry.getTitle(),
                favoriteEntry.getOriginalTitle(), favoriteEntry.getPosterPath(),
                favoriteEntry.getOverview(), favoriteEntry.getVoteAverage(),
                favoriteEntry.getReleaseDate());
    }

    private MoviesResponse convertToMoviesResponse(List<FavoriteEntry> favoriteEntryList) {

        MoviesResponse moviesResponse = new MoviesResponse();

        Movie[] movies = new Movie[favoriteEntryList.size()];

        for(int i = 0; i < favoriteEntryList.size(); i++) {
            movies[i] = convertToMovie(favoriteEntryList.get(i));
        }

        moviesResponse.setMovies(movies);

        return moviesResponse;
    }
}

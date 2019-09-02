package com.addisonlima.popularmovies.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.addisonlima.popularmovies.models.MoviesResponse;
import com.addisonlima.popularmovies.models.RequestStatus;
import com.addisonlima.popularmovies.models.RequestStatus.SortType;
import com.addisonlima.popularmovies.repository.TMDbRepository;

public class MoviesViewModel extends AndroidViewModel {

    private TMDbRepository mRepository;

    public MoviesViewModel(@NonNull Application application) {
        super(application);

        mRepository = TMDbRepository.getInstance(this.getApplication());
    }

    public LiveData<MoviesResponse> getMoviesResponse() {
        return mRepository.getMoviesResponse();
    }

    public LiveData<RequestStatus> getRequestStatus() {
        return mRepository.getRequestStatus();
    }

    public void sortMoviesBy(SortType sortType) {
        mRepository.sortMoviesBy(sortType);
    }
}

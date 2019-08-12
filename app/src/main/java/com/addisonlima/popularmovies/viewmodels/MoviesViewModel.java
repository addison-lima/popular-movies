package com.addisonlima.popularmovies.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.addisonlima.popularmovies.models.MoviesResponse;
import com.addisonlima.popularmovies.models.RequestStatus;
import com.addisonlima.popularmovies.models.RequestStatus.SortType;
import com.addisonlima.popularmovies.repository.TMDbRepository;

public class MoviesViewModel extends ViewModel {

    private TMDbRepository mRepository = new TMDbRepository();

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

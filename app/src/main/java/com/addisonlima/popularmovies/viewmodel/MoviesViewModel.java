package com.addisonlima.popularmovies.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.addisonlima.popularmovies.model.MoviesResponse;
import com.addisonlima.popularmovies.model.RequestStatus;
import com.addisonlima.popularmovies.model.RequestStatus.SortType;
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

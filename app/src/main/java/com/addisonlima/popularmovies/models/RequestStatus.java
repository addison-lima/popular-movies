package com.addisonlima.popularmovies.models;

public class RequestStatus {

    private final SortType mSortType;
    private final RequestState mRequestState;

    public RequestStatus(SortType sortType, RequestState requestState) {
        mSortType = sortType;
        mRequestState = requestState;
    }

    public SortType getSortType() {
        return mSortType;
    }

    public RequestState getRequestState() {
        return mRequestState;
    }

    public enum SortType {
        FAVORITE,
        POPULAR,
        TOP_RATED
    }

    public enum RequestState {
        EMPTY,
        LOADING,
        SUCCESS,
        FAILURE
    }
}
